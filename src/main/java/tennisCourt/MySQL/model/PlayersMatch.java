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
    int shots;
    int shots_on_target;
    int goals;
    int passes;
    int accurate_passes;
    int assists;
    int fouls;
    int red_cards;
    int yellow_cards;
    boolean injury;
    int integration_attempts;
    int integrations;
    int defence_attempts;
    int defences;

    public PlayersMatch() {
    }

    public PlayersMatch(PersonalData player, Matches match, int minutes, int goals, int assists) {
        this.minutes = minutes;
        this.distance = minutes * 10 * ThreadLocalRandom.current().nextInt(8, 15);
        this.shots = (int) ((((float) minutes) / 90) * ThreadLocalRandom.current().nextInt(0, 5));
        this.shots_on_target = this.shots - (this.shots * ThreadLocalRandom.current().nextInt(0, 11) / 10);
        if (goals<=0)
            this.goals = 0;
        else
            this.goals = ThreadLocalRandom.current().nextInt(0, goals+1);
        this.passes = (int) ((((float) minutes) / 90) * ThreadLocalRandom.current().nextInt(10, 40));
        this.accurate_passes = this.passes - (this.passes * ThreadLocalRandom.current().nextInt(0, 11) / 10);
        if (assists<=0)
            this.assists = 0;
        else
            this.assists = ThreadLocalRandom.current().nextInt(0, assists+1);
        this.fouls = (int) ((((float) minutes) / 90) * ThreadLocalRandom.current().nextInt(0, 4));
        this.red_cards =  ThreadLocalRandom.current().nextInt(0, 30)% 29==0 ? 1 : 0;
        this.yellow_cards = ThreadLocalRandom.current().nextInt(0, 5) %4==0 ? 1 : 0;
        this.injury = ThreadLocalRandom.current().nextInt(0, 100) % 99 == 0;
        this.integration_attempts =(int) ((((float) minutes) / 90) * ThreadLocalRandom.current().nextInt(0, 10));
        this.integrations = this.integration_attempts - (this.integration_attempts * ThreadLocalRandom.current().nextInt(0, 11) / 10);
        this.defence_attempts = (int) ((((float) minutes) / 90) * ThreadLocalRandom.current().nextInt(0, 11));
        this.defences =  this.defence_attempts - (this.defence_attempts * ThreadLocalRandom.current().nextInt(0, 11) / 10);
        int degreeValue = 3;
        degreeValue -= (this.red_cards + this.yellow_cards + (fouls/3));
        degreeValue = (this.distance<11000) ? degreeValue : (degreeValue+1);
        degreeValue += this.goals + this.assists;
        if(this.passes!=0)  degreeValue = (((float) this.accurate_passes/this.passes)>0.7) ? (degreeValue+1) : degreeValue;
        if(this.shots!=0)  degreeValue = (((float) this.shots_on_target/this.shots)>0.7) ? (degreeValue+1) : degreeValue;
        if(this.integration_attempts!=0) degreeValue = (((float) this.integrations/this.integration_attempts)>0.7) ? (degreeValue+1) : degreeValue;
        if(this.defence_attempts!=0) degreeValue = (((float) this.defences/this.defence_attempts)>0.7) ? (degreeValue+1) : degreeValue;
        if(this.passes!=0) degreeValue = (((float) this.accurate_passes/this.passes)<0.3) ? (degreeValue-1) : degreeValue;
        if(this.shots!=0) degreeValue = (((float) this.shots_on_target/this.shots)<0.3) ? (degreeValue-1) : degreeValue;
        if(this.integration_attempts!=0) degreeValue = (((float) this.integrations/this.integration_attempts)<0.3) ? (degreeValue-1) : degreeValue;
        if(this.defence_attempts!=0) degreeValue = (((float) this.defences/this.defence_attempts)<0.3) ? (degreeValue-1) : degreeValue;
        if(degreeValue>6)
            degreeValue=6;
        if(degreeValue<0)
            degreeValue=0;
        this.degree = degreeValue;
        this.player = player;
        this.matches = match;
    }

    public PlayersMatch(PersonalData player, Matches match, int minutes) {
        this.goals = 0;
        this.assists = 0;
        this.minutes = minutes;
        this.distance = minutes * 10 * ThreadLocalRandom.current().nextInt(8, 15);
        this.shots = (int) ((((float) minutes) / 90) * ThreadLocalRandom.current().nextInt(0, 5));
        this.shots_on_target = this.shots - (this.shots * ThreadLocalRandom.current().nextInt(0, 11) / 10);
        this.passes = (int) ((((float) minutes) / 90) * ThreadLocalRandom.current().nextInt(10, 40));
        this.accurate_passes = this.passes - (this.passes * ThreadLocalRandom.current().nextInt(0, 11) / 10);
        this.fouls = (int) ((((float) minutes) / 90) * ThreadLocalRandom.current().nextInt(0, 4));
        this.red_cards =  ThreadLocalRandom.current().nextInt(0, 30)% 29==0 ? 1 : 0;
        this.yellow_cards = ThreadLocalRandom.current().nextInt(0, 5) %4==0 ? 1 : 0;
        this.injury = ThreadLocalRandom.current().nextInt(0, 100) % 99 == 0;
        this.integration_attempts =(int) ((((float) minutes) / 90) * ThreadLocalRandom.current().nextInt(0, 10));
        this.integrations = this.integration_attempts - (this.integration_attempts * ThreadLocalRandom.current().nextInt(0, 11) / 10);
        this.defence_attempts = (int) ((((float) minutes) / 90) * ThreadLocalRandom.current().nextInt(0, 11));
        this.defences =  this.defence_attempts - (this.defence_attempts * ThreadLocalRandom.current().nextInt(0, 11) / 10);
        int degreeValue = 3;
        degreeValue -= (this.red_cards + this.yellow_cards + (fouls/3));
        degreeValue = (this.distance<11000) ? degreeValue : (degreeValue+1);
        if(this.passes!=0)  degreeValue = (((float) this.accurate_passes/this.passes)>0.7) ? degreeValue : (degreeValue+1);
        if(this.shots!=0)  degreeValue = (((float) this.shots_on_target/this.shots)>0.7) ? degreeValue : (degreeValue+1);
        if(this.integration_attempts!=0) degreeValue = (((float) this.integrations/this.integration_attempts)>0.7) ? degreeValue : (degreeValue+1);
        if(this.defence_attempts!=0) degreeValue = (((float) this.defences/this.defence_attempts)>0.7) ? degreeValue : (degreeValue+1);
        if(this.passes!=0) degreeValue = (((float) this.accurate_passes/this.passes)<0.3) ? degreeValue : (degreeValue-1);
        if(this.shots!=0) degreeValue = (((float) this.shots_on_target/this.shots)<0.3) ? degreeValue : (degreeValue-1);
        if(this.integration_attempts!=0) degreeValue = (((float) this.integrations/this.integration_attempts)<0.3) ? degreeValue : (degreeValue-1);
        if(this.defence_attempts!=0) degreeValue = (((float) this.defences/this.defence_attempts)<0.3) ? degreeValue : (degreeValue-1);
        if(degreeValue>6)
            degreeValue=6;
        if(degreeValue<0)
            degreeValue=0;
        this.degree = degreeValue;
        this.player = player;
        this.matches = match;
    }

    public PlayersMatch(float deegree, int minutes, float distance, int shots, int shots_on_target, int goals,
                        int passes, int accurate_passes, int assists, int fouls, int red_cards, int yellow_cards,
                        boolean injury, int integration_attempts, int integrations, int defence_attempts, int defences,
                        PersonalData player, Matches match) {
        this.degree = deegree;
        this.minutes = minutes;
        this.distance = distance;
        this.shots = shots;
        this.shots_on_target = shots_on_target;
        this.goals = goals;
        this.passes = passes;
        this.accurate_passes = accurate_passes;
        this.assists = assists;
        this.fouls = fouls;
        this.red_cards = red_cards;
        this.yellow_cards = yellow_cards;
        this.injury = injury;
        this.integration_attempts = integration_attempts;
        this.integrations = integrations;
        this.defence_attempts = defence_attempts;
        this.defences = defences;
        this.player = player;
        this.matches = match;
        this.player = player;
        this.matches = match;
    }
}
