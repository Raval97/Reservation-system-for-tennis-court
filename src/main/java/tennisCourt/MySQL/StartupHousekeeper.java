package tennisCourt.MySQL;

import com.github.dhiraj072.randomwordgenerator.exceptions.DataMuseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import tennisCourt.MySQL.model.*;
import tennisCourt.MySQL.repo.*;

import java.util.ArrayList;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;


@Component
public class StartupHousekeeper {

    private CompetitionRepo competitionRepo;
    private MatchCompetitionsRepo matchCompetitionsRepo;
    private MatchRepo matchRepo;
    private PersonalDataRepo personalDataRepo;
    private PlayersMatchRepo playersMatchRepo;
    private PlayerTeamRepo playerTeamRepo;
    private TeamRepo teamRepo;

    public StartupHousekeeper() {
    }

    @Autowired
    public StartupHousekeeper(CompetitionRepo competitionRepo, MatchCompetitionsRepo matchCompetitionsRepo,
                   MatchRepo matchRepo, PersonalDataRepo personalDataRepo, PlayersMatchRepo playersMatchRepo,
                   PlayerTeamRepo playerTeamRepo, TeamRepo teamRepo) {
        this.competitionRepo = competitionRepo;
        this.matchCompetitionsRepo = matchCompetitionsRepo;
        this.matchRepo = matchRepo;
        this.personalDataRepo = personalDataRepo;
        this.playersMatchRepo = playersMatchRepo;
        this.playerTeamRepo = playerTeamRepo;
        this.teamRepo = teamRepo;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void contextRefreshedEvent() throws DataMuseException {
        System.out.println("Start");
        int teamsSize = 16;
        int playersSize = 22;
        int matchesSize = 1000;
        List<Team> teams = new ArrayList<Team>();
        List<PersonalData> players = new ArrayList<PersonalData>();
        for (int i = 0; i < teamsSize; i++) {
            teams.add(new Team());
        }
        int iter1 = 1;
        for (Team t : teams) {
            teamRepo.save(t);
            System.out.println("team nr: " + iter1++);
            int iter2 = 1;
            for (int i = 0; i < playersSize; i++) {
                System.out.println("player nr: " + iter2++);
                PlayerTeam pt = new PlayerTeam(t);
                PersonalData p = new PersonalData(pt);
                players.add(p);
                personalDataRepo.save(p);
            }
        }
        Competition c1 = new Competition();
        competitionRepo.save(c1);
        int iter3 = 1;
        for (int i = 0; i < matchesSize; i++) {
            System.out.println("match nr: " + iter3++);
            MatchCompetitions mc1 = new MatchCompetitions(c1);
            Team t1 = teams.get(ThreadLocalRandom.current().nextInt(0, teamsSize));
            Team t2 = teams.get(ThreadLocalRandom.current().nextInt(0, teamsSize));
            if (t1.equals(t2))
                while (t1.equals(t2))
                    t2 = teams.get(ThreadLocalRandom.current().nextInt(0, teamsSize));

            Matches m = new Matches(mc1, t1, t2);
            matchRepo.save(m);
            List<PersonalData> playerT1 = new ArrayList<>();
            playerT1 = players.stream().filter(p -> p.getPlayerTeam().getTeam().getId().equals(t1.getId())).collect(Collectors.toList());;
            List<PersonalData> playerT2 = new ArrayList<>();
            Team finalT2 = t2;
            playerT2 = players.stream().filter(p -> p.getPlayerTeam().getTeam().getId().equals(finalT2.getId())).collect(Collectors.toList());;
            AtomicInteger iter4 = new AtomicInteger(1);
            playerT1.forEach(p -> {
                System.out.println("player match details nr: " + iter4.getAndIncrement());
                playersMatchRepo.save(new PlayersMatch(p, m));
            });
            playerT2.forEach(p -> playersMatchRepo.save(new PlayersMatch(p, m)));
        }
        System.out.println("END");
    }

}

