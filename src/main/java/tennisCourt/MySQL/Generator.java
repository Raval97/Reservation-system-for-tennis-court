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
import java.util.stream.Collectors;


@Component
public class Generator {

    private CompetitionRepo competitionRepo;
    private CompetitionTeamRepo competitionTeamRepo;
    private MatchCompetitionsRepo matchCompetitionsRepo;
    private MatchRepo matchRepo;
    private PersonalDataRepo personalDataRepo;
    private PlayersMatchRepo playersMatchRepo;
    private PlayerMatchPerformanceRepo playerMatchPerformancerepo;
    private PlayerTeamRepo playerTeamRepo;
    private TeamRepo teamRepo;

    public Generator() throws DataMuseException {
    }

    @Autowired
    public Generator(CompetitionRepo competitionRepo, MatchCompetitionsRepo matchCompetitionsRepo,
                     MatchRepo matchRepo, PersonalDataRepo personalDataRepo, PlayersMatchRepo playersMatchRepo,
                     PlayerTeamRepo playerTeamRepo, TeamRepo teamRepo, PlayerMatchPerformanceRepo playerMatchPerformancerepo,
                     CompetitionTeamRepo competitionTeamRepo) throws DataMuseException {
        this.competitionRepo = competitionRepo;
        this.matchCompetitionsRepo = matchCompetitionsRepo;
        this.matchRepo = matchRepo;
        this.personalDataRepo = personalDataRepo;
        this.playersMatchRepo = playersMatchRepo;
        this.playerTeamRepo = playerTeamRepo;
        this.teamRepo = teamRepo;
        this.competitionTeamRepo = competitionTeamRepo;
        this.playerMatchPerformancerepo = playerMatchPerformancerepo;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void contextRefreshedEvent() throws DataMuseException {
        System.out.println("Start");
        int teamsSize = 10;
        int playersSize = 22;
        int matchesSize = 10;
        List<Competition> competitions = new ArrayList<Competition>();
        List<Team> teams = new ArrayList<Team>();
        List<Player> players = new ArrayList<Player>();

        Competition c1 = new Competition("La liga", "1", "zimowa", 25);
        Competition c2 = new Competition("La liga", "1", "wiosenna", 25);
        Competition c3 = new Competition("Copa Del Ray", "1", "none", 25);
        competitions.add(c1); competitions.add(c2); competitions.add(c3);

        for (int i = 0; i < teamsSize; i++)
            teams.add(new Team());
        int iter1 = 1;
        for (Team t : teams) {
            teamRepo.save(t);
            System.out.println("team nr: " + iter1++);
            for (int i = 0; i < playersSize; i++) {
//                System.out.println("player nr: " + (i+1));
                PlayerTeam pt = new PlayerTeam(t);
                Player p = new Player(pt);
                players.add(p);
                personalDataRepo.save(p);
            }
        }

        int iter2= 0;
        for (Competition c: competitions) {
            competitionRepo.save(c);
            for (Team t : teams)
                competitionTeamRepo.save(new CompetitionTeam(c, t));
            iter2++;
            for (int i = 0; i < matchesSize; i++) {
                System.out.println("Competition: "+ iter2 + " match nr: " + (i+1));
                MatchCompetitions mc1 = new MatchCompetitions(c);
                Team t1 = teams.get(ThreadLocalRandom.current().nextInt(0, teamsSize));
                Team t2 = teams.get(ThreadLocalRandom.current().nextInt(0, teamsSize));
                if (t1.equals(t2))
                    while (t1.equals(t2))
                        t2 = teams.get(ThreadLocalRandom.current().nextInt(0, teamsSize));

                Matches m = new Matches(mc1, t1, t2);
                matchRepo.save(m);
                List<Player> playerT1 = new ArrayList<>();
                playerT1 = players.stream().filter(p -> p.getPlayerTeam().getTeam().getId().equals(t1.getId())).collect(Collectors.toList());
                Collections.shuffle(playerT1);
                List<Player> playerT2 = new ArrayList<>();
                Team finalT2 = t2;
                playerT2 = players.stream().filter(p -> p.getPlayerTeam().getTeam().getId().equals(finalT2.getId())).collect(Collectors.toList());
                Collections.shuffle(playerT2);

                int goalsT1 = m.getTeam_1_score();
                int goalsT2 = m.getTeam_2_score();
                int assistsT1 = goalsT1;
                int assistsT2 = goalsT2;
                for (int j = 0; j < 14; j++) {
                    int minutes = ThreadLocalRandom.current().nextInt(5, 46);
                    PlayerMatchPerformance pmp1 = new PlayerMatchPerformance(playerT1.get(j), m);
                    PlayerMatchPerformance pmp2 = new PlayerMatchPerformance(playerT2.get(j), m);
                    if(j<=8) {
                            Performance p1 = new Performance(pmp1, 90, goalsT1, assistsT1);
                            Performance p2 = new Performance(pmp2, 90, goalsT2, assistsT2);
                            goalsT1 -= p1.getGoals();
                            goalsT2 -= p2.getGoals();
                            assistsT1 -= p1.getAssists();
                            assistsT2 -= p2.getAssists();
                            playersMatchRepo.save(p1);
                            playersMatchRepo.save(p2);
                    }
                    else if(j>8 && j<=11) {
                        playersMatchRepo.save(new Performance(pmp1, (90-minutes)));
                        playersMatchRepo.save(new Performance(pmp2, (90-minutes)));
                    }
                    else {
                        playersMatchRepo.save(new Performance(pmp1, minutes));
                        playersMatchRepo.save(new Performance(pmp2, minutes));
                    }
//                System.out.println("player match details: " + (j+1));
                }
            }
        }
        System.out.println("END");
//        Competition com =  new Competition("dsdf");
//        MatchCompetitions matchCompetitions = new MatchCompetitions(com);
//        Team team1 = new Team();
//        Team team2 = new Team();
//        PlayerTeam pt1 = new PlayerTeam(team1);
//        PersonalData pe = new PersonalData(pt1);
//        Matches match = new Matches(matchCompetitions, team1, team2);
//        PlayersMatch playersMatch1 = new PlayersMatch(pe, match, 90, 2, 4);
//        PlayersMatch playersMatch2 = new PlayersMatch(pe, match, 60, 2, 4);
//        System.out.println("adfsfsdf");
    }


}

