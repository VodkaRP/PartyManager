package me.serenia.partymanager.party;



import lombok.Data;
import me.serenia.partymanager.Utils;
import me.serenia.partymanager.player.Manager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.LinkedList;
import java.util.UUID;
import static me.serenia.partymanager.PartyManager.*;
@Data
public class Party {
     UUID partyLeader;
     LinkedList<UUID> members;
     boolean roundRobin;
     boolean ffa;
     boolean lastHit;
    public Party(UUID partyLeader, UUID... mms){
        this.partyLeader = partyLeader;
        LinkedList<UUID> members = new LinkedList<>();
        members.add(partyLeader);
        Collections.addAll(members, mms);
        this.members = members;
        Player p = Bukkit.getPlayer(partyLeader);
        this.roundRobin = Manager.getValue(p, "roundRobin");
        this.ffa = Manager.getValue(p, "ffa");
        this.lastHit = Manager.getValue(p, "lastHit");
        parties.add(this);
    }
    public int size(){
        return members.size();
    }

    public void kick(Player p){
        UUID id = p.getUniqueId();
        if (id == partyLeader){

        }
        members.remove(p.getUniqueId());

    }
    public void add(Player p){
        members.add(p.getUniqueId());
        broadCastMessage(Utils.getString("join-message-bc", p.getName()));
    }
    public boolean isLeader(Player p){
        return p.getUniqueId() == partyLeader;
    }
    public void cleanUp(){
        members.removeIf(uuid -> (Bukkit.getPlayer(uuid) == null));
        if (size() > 2){
           if (!parties.contains(this)) return;
           parties.remove(this);
        } else {
            partyLeader = members.get(0);
        }
    }
    public void broadCastMessage(String s){
        for (Player p : getPlayers()){
            p.sendMessage(Utils.chat(s));
        }
    }
    public LinkedList<Player> getPlayers(){
        LinkedList<Player> players = new LinkedList<>();
        for (UUID id : members){
            Player p = Bukkit.getPlayer(id);
            if (p != null) players.add(p); else cleanUp();
        }
        return players;
    }
    public boolean hasPlayer(Player p){
        return members.contains(p.getUniqueId());
    }
}
