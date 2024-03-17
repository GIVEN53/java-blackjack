package domain.participant;

import domain.participant.betting.BetAmount;
import domain.participant.name.Name;

public class Player extends Participant {
    private final BetAmount betAmount;

    public Player(Name name, BetAmount betAmount) {
        super(name);
        this.betAmount = betAmount;
    }

    @Override
    public boolean isNotFinished() {
        return !isBust() && !isBlackjack();
    }

    public int getBetAmount() {
        return betAmount.getValue();
    }
}
