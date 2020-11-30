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
        List<Team> teams = new ArrayList<Team>();
        List<PersonalData> players = new ArrayList<PersonalData>();
        for (int i = 0; i < 2; i++) {
            teams.add(new Team());
        }
        System.out.println(teams.size());
        for (Team t : teams) {
            teamRepo.save(t);
            for (int i = 0; i < 2; i++) {
                PlayerTeam pt = new PlayerTeam(t);
                PersonalData p = new PersonalData(pt);
                players.add(p);
                personalDataRepo.save(p);
            }
        }
        Competition c1 = new Competition();
        competitionRepo.save(c1);
        for (int i = 0; i < 2; i++) {
            MatchCompetitions mc1 = new MatchCompetitions(c1);
            Team t1 = teams.get(ThreadLocalRandom.current().nextInt(0, teams.size()));
            Team t2 = teams.get(ThreadLocalRandom.current().nextInt(0, teams.size()));
            if (t1.equals(t2))
                while (t1.equals(t2))
                    t2 = teams.get(ThreadLocalRandom.current().nextInt(0, teams.size()));

            Matches m = new Matches(mc1, t1, t2);
            matchRepo.save(m);
            List<PersonalData> playerT1 = new ArrayList<>();
            players.stream().filter(p -> p.getPlayerTeam().equals(t1)).toArray();
            List<PersonalData> playerT2 = new ArrayList<>();
            Team finalT2 = t2;
            players.stream().filter(p -> p.getPlayerTeam().equals(finalT2)).toArray();
            playerT1.forEach(p -> playersMatchRepo.save(new PlayersMatch(p, m)));
            playerT2.forEach(p -> playersMatchRepo.save(new PlayersMatch(p, m)));
        }
        System.out.println("END");
    }

}