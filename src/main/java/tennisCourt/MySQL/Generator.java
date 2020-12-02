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
public class Generator {

    private CompetitionRepo competitionRepo;
    private MatchCompetitionsRepo matchCompetitionsRepo;
    private MatchRepo matchRepo;
    private PersonalDataRepo personalDataRepo;
    private PlayersMatchRepo playersMatchRepo;
    private PlayerTeamRepo playerTeamRepo;
    private TeamRepo teamRepo;
    private StatisticsRepo statisticsRepo;
    private EffectiveStatisticsRepo effectiveStatisticsRepo;
    private RefereeStatisticsRepo refereeStatisticsRepo;

    public Generator() throws DataMuseException {
    }

    @Autowired
    public Generator(CompetitionRepo competitionRepo, MatchCompetitionsRepo matchCompetitionsRepo,
                     MatchRepo matchRepo, PersonalDataRepo personalDataRepo, PlayersMatchRepo playersMatchRepo,
                     PlayerTeamRepo playerTeamRepo, TeamRepo teamRepo, StatisticsRepo statisticsRepo,
                     EffectiveStatisticsRepo effectiveStatisticsRepo, RefereeStatisticsRepo refereeStatisticsRepo) throws DataMuseException {
        this.competitionRepo = competitionRepo;
        this.matchCompetitionsRepo = matchCompetitionsRepo;
        this.matchRepo = matchRepo;
        this.personalDataRepo = personalDataRepo;
        this.playersMatchRepo = playersMatchRepo;
        this.playerTeamRepo = playerTeamRepo;
        this.teamRepo = teamRepo;
        this.statisticsRepo = statisticsRepo;
        this.effectiveStatisticsRepo = effectiveStatisticsRepo;
        this.refereeStatisticsRepo = refereeStatisticsRepo;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void contextRefreshedEvent() throws DataMuseException {
        System.out.println("Start");
        int teamsSize = 3;
        int playersSize = 22;
        int matchesSize = 10;
        List<Competition> competitions = new ArrayList<Competition>();
        List<Team> teams = new ArrayList<Team>();
        List<PersonalData> players = new ArrayList<PersonalData>();

        for (int i = 0; i < teamsSize; i++)
            teams.add(new Team());
        int iter1 = 1;
        for (Team t : teams) {
            teamRepo.save(t);
            System.out.println("team nr: " + iter1++);
            for (int i = 0; i < playersSize; i++) {
//                System.out.println("player nr: " + (i+1));
                PlayerTeam pt = new PlayerTeam(t);
                PersonalData p = new PersonalData(pt);
                players.add(p);
                personalDataRepo.save(p);
            }
        }

        Competition c1 = new Competition("Ekstraklasa", "1", "zimowa", 25);
        Competition c2 = new Competition("Ekstraklasa", "1", "wiosenna", 25);
        Competition c3 = new Competition("Puchar Polski", "1", "none", 25);
        competitions.add(c1); competitions.add(c2); competitions.add(c3);
        int iter2= 0;
        for (Competition c: competitions) {
            competitionRepo.save(c);
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
                List<PersonalData> playerT1 = new ArrayList<>();
                playerT1 = players.stream().filter(p -> p.getPlayerTeam().getTeam().getId().equals(t1.getId())).collect(Collectors.toList());
                Collections.shuffle(playerT1);
                List<PersonalData> playerT2 = new ArrayList<>();
                Team finalT2 = t2;
                playerT2 = players.stream().filter(p -> p.getPlayerTeam().getTeam().getId().equals(finalT2.getId())).collect(Collectors.toList());
                Collections.shuffle(playerT2);

                int goalsT1 = m.getTeam_1_score();
                int goalsT2 = m.getTeam_2_score();
                int assistsT1 = goalsT1;
                int assistsT2 = goalsT2;
                for (int j = 0; j < 14; j++) {
                    Statistics s1 = new Statistics();
                    Statistics s2 = new Statistics();
                    EffectiveStatistics es1 = new EffectiveStatistics();
                    EffectiveStatistics es2 = new EffectiveStatistics();
                    RefereeStatistics rs1 = new RefereeStatistics();
                    RefereeStatistics rs2 = new RefereeStatistics();
                    int minutes = ThreadLocalRandom.current().nextInt(5, 46);
                    if(j<=8) {
                            PlayersMatch p1 = new PlayersMatch(playerT1.get(j), m, 90, goalsT1, assistsT1, s1, es1, rs1);
                            PlayersMatch p2 = new PlayersMatch(playerT2.get(j), m, 90, goalsT2, assistsT2, s2, es2, rs2);
                            goalsT1 -= rs1.getGoals();
                            goalsT2 -= rs2.getGoals();
                            assistsT1 -= rs1.getAssists();
                            assistsT2 -= rs2.getAssists();
                            playersMatchRepo.save(p1);
                            playersMatchRepo.save(p2);
                    }
                    else if(j>8 && j<=11) {
                        playersMatchRepo.save(new PlayersMatch(playerT1.get(j), m, (90-minutes), s1, es1, rs1));
                        playersMatchRepo.save(new PlayersMatch(playerT2.get(j), m, (90-minutes), s2, es2, rs2));
                    }
                    else {
                        playersMatchRepo.save(new PlayersMatch(playerT1.get(j), m, minutes, s1, es1, rs1));
                        playersMatchRepo.save(new PlayersMatch(playerT2.get(j), m, minutes, s2, es2, rs2));
                    }
//                System.out.println("player match details: " + (j+1));
                }
            }
        }
        System.out.println("END");
    }


}

