package tennisCourt.MySQL.model;

import lombok.Data;

import javax.persistence.*;
import java.util.concurrent.ThreadLocalRandom;

@Data
@Entity
@Table(name = "players_match")
public class PlayersMatch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn
    private PersonalData player;
    @ManyToOne
    @JoinColumn
    private Matches matches;
    float degree;
    int minutes;
    float distance;
    boolean injury;
    @OneToOne(mappedBy = "playersMatch", cascade = CascadeType.ALL)
    private Statistics statistics ;
    @OneToOne(mappedBy = "playersMatch", cascade = CascadeType.ALL)
    private EffectiveStatistics effectiveStatistics ;
    @OneToOne(mappedBy = "playersMatch", cascade = CascadeType.ALL)
    private RefereeStatistics refereeStatistics;

    public PlayersMatch() {
    }

    public PlayersMatch(PersonalData player, Matches match, int minutes, int goals, int assists,
                        Statistics statistics, EffectiveStatistics effectiveStatistics,
                        RefereeStatistics refereeStatistics) {
        this.minutes = minutes;
        this.distance = minutes * 10 * ThreadLocalRandom.current().nextInt(8, 15);
        int shots = (int) ((((float) minutes) / 90) * ThreadLocalRandom.current().nextInt(0, 5));
        int shots_on_target = shots - (shots * ThreadLocalRandom.current().nextInt(0, 11) / 10);
        int goal = 0;
        if (goals<=0)
            goal = 0;
        else
            goal = ThreadLocalRandom.current().nextInt(0, goals+1);
        int passes = (int) ((((float) minutes) / 90) * ThreadLocalRandom.current().nextInt(10, 40));
        int accurate_passes = passes - (passes * ThreadLocalRandom.current().nextInt(0, 11) / 10);
        int assist = 0;
        if (assists<=0)
            assist = 0;
        else
            assist = ThreadLocalRandom.current().nextInt(0, assists+1);
        int fouls = (int) ((((float) minutes) / 90) * ThreadLocalRandom.current().nextInt(0, 4));
        int red_cards =  ThreadLocalRandom.current().nextInt(0, 30)% 29==0 ? 1 : 0;
        int yellow_cards = ThreadLocalRandom.current().nextInt(0, 5) %4==0 ? 1 : 0;
        this.injury = ThreadLocalRandom.current().nextInt(0, 100) % 99 == 0;
        int integration_attempts =(int) ((((float) minutes) / 90) * ThreadLocalRandom.current().nextInt(0, 10));
        int integrations =integration_attempts - (integration_attempts * ThreadLocalRandom.current().nextInt(0, 11) / 10);
        int defence_attempts = (int) ((((float) minutes) / 90) * ThreadLocalRandom.current().nextInt(0, 11));
        int defences =  defence_attempts - (defence_attempts * ThreadLocalRandom.current().nextInt(0, 11) / 10);
        int degreeValue = 3;
        degreeValue -= (red_cards + yellow_cards + (fouls/3));
        degreeValue = (this.distance<11000) ? degreeValue : (degreeValue+1);
        degreeValue += goal + assist;
        if(passes!=0)  degreeValue = (((float) accurate_passes/passes)>0.7) ? (degreeValue+1) : degreeValue;
        if(shots!=0)  degreeValue = (((float) shots_on_target/shots)>0.7) ? (degreeValue+1) : degreeValue;
        if(integration_attempts!=0) degreeValue = (((float) integrations/integration_attempts)>0.7) ? (degreeValue+1) : degreeValue;
        if(defence_attempts!=0) degreeValue = (((float) defences/defence_attempts)>0.7) ? (degreeValue+1) : degreeValue;
        if(passes!=0) degreeValue = (((float) accurate_passes/passes)<0.3) ? (degreeValue-1) : degreeValue;
        if(shots!=0) degreeValue = (((float) shots_on_target/shots)<0.3) ? (degreeValue-1) : degreeValue;
        if(integration_attempts!=0) degreeValue = (((float) integrations/integration_attempts)<0.3) ? (degreeValue-1) : degreeValue;
        if(defence_attempts!=0) degreeValue = (((float) defences/defence_attempts)<0.3) ? (degreeValue-1) : degreeValue;
        if(degreeValue>6)
            degreeValue=6;
        if(degreeValue<0)
            degreeValue=0;
        this.degree = degreeValue;
        this.player = player;
        this.matches = match;
        statistics.setShots(shots);
        statistics.setPasses(passes);
        statistics.setInterceptions_attempts(integration_attempts);
        statistics.setDefences_attempts(defence_attempts);
        effectiveStatistics.setShots_on_target(shots_on_target);
        effectiveStatistics.setAccurate_passes(accurate_passes);
        effectiveStatistics.setInterceptions(integrations);
        effectiveStatistics.setDefences(defences);
        refereeStatistics.setAssists(assist);
        refereeStatistics.setGoals(goal);
        refereeStatistics.setFouls(fouls);
        refereeStatistics.setYellow_cards(yellow_cards);
        refereeStatistics.setRed_cards(red_cards);
        this.statistics = statistics;
        this.statistics.setPlayersMatch(this);
        this.effectiveStatistics = effectiveStatistics;
        this.effectiveStatistics.setPlayersMatch(this);
        this.refereeStatistics = refereeStatistics;
        this.refereeStatistics.setPlayersMatch(this);
    }

    public PlayersMatch(PersonalData player, Matches match, int minutes, Statistics statistics, EffectiveStatistics effectiveStatistics,
                        RefereeStatistics refereeStatistics) {
        this.minutes = minutes;
        this.distance = minutes * 10 * ThreadLocalRandom.current().nextInt(8, 15);
        int shots = (int) ((((float) minutes) / 90) * ThreadLocalRandom.current().nextInt(0, 5));
        int shots_on_target = shots - (shots * ThreadLocalRandom.current().nextInt(0, 11) / 10);
        int goal = 0;
        int passes = (int) ((((float) minutes) / 90) * ThreadLocalRandom.current().nextInt(10, 40));
        int accurate_passes = passes - (passes * ThreadLocalRandom.current().nextInt(0, 11) / 10);
        int assist = 0;
        int fouls = (int) ((((float) minutes) / 90) * ThreadLocalRandom.current().nextInt(0, 4));
        int red_cards =  ThreadLocalRandom.current().nextInt(0, 30)% 29==0 ? 1 : 0;
        int yellow_cards = ThreadLocalRandom.current().nextInt(0, 5) %4==0 ? 1 : 0;
        this.injury = ThreadLocalRandom.current().nextInt(0, 100) % 99 == 0;
        int integration_attempts =(int) ((((float) minutes) / 90) * ThreadLocalRandom.current().nextInt(0, 10));
        int integrations =integration_attempts - (integration_attempts * ThreadLocalRandom.current().nextInt(0, 11) / 10);
        int defence_attempts = (int) ((((float) minutes) / 90) * ThreadLocalRandom.current().nextInt(0, 11));
        int defences =  defence_attempts - (defence_attempts * ThreadLocalRandom.current().nextInt(0, 11) / 10);
        int degreeValue = 3;
        degreeValue -= (red_cards + yellow_cards + (fouls/3));
        degreeValue = (this.distance<11000) ? degreeValue : (degreeValue+1);
        if(passes!=0)  degreeValue = (((float) accurate_passes/passes)>0.7) ? (degreeValue+1) : degreeValue;
        if(shots!=0)  degreeValue = (((float) shots_on_target/shots)>0.7) ? (degreeValue+1) : degreeValue;
        if(integration_attempts!=0) degreeValue = (((float) integrations/integration_attempts)>0.7) ? (degreeValue+1) : degreeValue;
        if(defence_attempts!=0) degreeValue = (((float) defences/defence_attempts)>0.7) ? (degreeValue+1) : degreeValue;
        if(passes!=0) degreeValue = (((float) accurate_passes/passes)<0.3) ? (degreeValue-1) : degreeValue;
        if(shots!=0) degreeValue = (((float) shots_on_target/shots)<0.3) ? (degreeValue-1) : degreeValue;
        if(integration_attempts!=0) degreeValue = (((float) integrations/integration_attempts)<0.3) ? (degreeValue-1) : degreeValue;
        if(defence_attempts!=0) degreeValue = (((float) defences/defence_attempts)<0.3) ? (degreeValue-1) : degreeValue;
        if(degreeValue>6)
            degreeValue=6;
        if(degreeValue<0)
            degreeValue=0;
        this.degree = degreeValue;
        this.player = player;
        this.matches = match;
        statistics.setShots(shots);
        statistics.setPasses(passes);
        statistics.setInterceptions_attempts(integration_attempts);
        statistics.setDefences_attempts(defence_attempts);
        effectiveStatistics.setShots_on_target(shots_on_target);
        effectiveStatistics.setAccurate_passes(accurate_passes);
        effectiveStatistics.setInterceptions(integrations);
        effectiveStatistics.setDefences(defences);
        refereeStatistics.setAssists(assist);
        refereeStatistics.setGoals(goal);
        refereeStatistics.setFouls(fouls);
        refereeStatistics.setYellow_cards(yellow_cards);
        refereeStatistics.setRed_cards(red_cards);
        this.statistics = statistics;
        this.statistics.setPlayersMatch(this);
        this.effectiveStatistics = effectiveStatistics;
        this.effectiveStatistics.setPlayersMatch(this);
        this.refereeStatistics = refereeStatistics;
        this.refereeStatistics.setPlayersMatch(this);
    }

    public PlayersMatch(float deegree, int minutes, float distance, int shots, int shots_on_target, int goals,
                        int passes, int accurate_passes, int assists, int fouls, int red_cards, int yellow_cards,
                        boolean injury, int integration_attempts, int integrations, int defence_attempts, int defences,
                        PersonalData player, Matches match) {
        this.degree = deegree;
        this.minutes = minutes;
        this.distance = distance;
        this.injury = injury;
        this.player = player;
        this.matches = match;
        this.player = player;
        this.matches = match;
    }
}
