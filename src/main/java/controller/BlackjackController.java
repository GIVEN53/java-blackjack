package controller;

import domain.blackjackgame.BlackjackGame;
import domain.blackjackgame.GameResult;
import domain.card.deck.CardDeck;
import domain.card.deck.CardDeckFactory;
import domain.participant.BettingAmount;
import domain.participant.Dealer;
import domain.participant.Name;
import domain.participant.Names;
import domain.participant.Participants;
import domain.participant.Player;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import ui.InputView;
import ui.OutputView;

public class BlackjackController {
    private final InputView inputView;
    private final OutputView outputView;

    public BlackjackController(InputView inputView, OutputView outputView) {
        this.inputView = inputView;
        this.outputView = outputView;
    }

    public void start() {
        Participants participants = generateParticipants();
        CardDeck cardDeck = CardDeckFactory.createCardDeck(Collections::shuffle);
        BlackjackGame blackjackGame = new BlackjackGame(cardDeck);
        GameResult gameResult = playBlackjackGame(participants, blackjackGame);
        outputView.printGameResult(gameResult);
    }

    private Participants generateParticipants() {
        Names names = new Names(inputView.readPlayerNames());
        List<Player> players = new ArrayList<>();
        for (Name name : names.getNames()) {
            BettingAmount bettingAmount = new BettingAmount(inputView.readBettingAmount(name.getValue()));
            players.add(new Player(name, bettingAmount));
        }
        return new Participants(players);
    }

    private GameResult playBlackjackGame(Participants participants, BlackjackGame blackjackGame) {
        blackjackGame.initGame(participants);
        outputView.printInitialCards(participants);

        for (Player player : participants.getPlayers()) {
            dealToPlayer(player, blackjackGame);
        }
        dealToDealer(participants.getDealer(), blackjackGame);
        outputView.printCardsWithScore(participants);
        return blackjackGame.createGameResult(participants);
    }

    private void dealToPlayer(Player player, BlackjackGame blackjackGame) {
        while (player.isNotFinished() && inputView.askForMoreCard(player.getName())) {
            blackjackGame.dealCardTo(player);
            outputView.printAllCards(player);
        }
    }

    private void dealToDealer(Dealer dealer, BlackjackGame blackjackGame) {
        while (dealer.isNotFinished()) {
            blackjackGame.dealCardTo(dealer);
            outputView.printDealerReceiveCardMessage();
        }
    }
}
