package com.example.muk_jji_ppa.controller;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class MukJjiPpaController implements CommandLineRunner {

    private final Scanner scanner = new Scanner(System.in);
    private final String[] choices = {"묵", "찌", "빠"};
    private final Map<String, String> beats = Map.of("묵", "찌", "찌", "빠", "빠", "묵");

    private enum Result {WIN, LOSE, DRAW}

    private static class RoundResult {
        int roundNumber;
        String playerChoice;
        String computerChoice;
        String result;

        RoundResult(int roundNumber, String playerChoice, String computerChoice, String result) {
            this.roundNumber = roundNumber;
            this.playerChoice = playerChoice;
            this.computerChoice = computerChoice;
            this.result = result;
        }
    }

    @Override
    public void run(String... args) {
        System.out.print("총 게임 라운드 수를 입력하세요: ");
        int totalRounds = readValidInt();
        System.out.println("총 " + totalRounds + " 라운드를 진행합니다.");

        List<RoundResult> results = new ArrayList<>();
        int playerWins = 0;
        int computerWins = 0;

        for (int round = 1; round <= totalRounds; round++) {
            System.out.println("\n=== 라운드 " + round + " 시작 ===");
            String attacker = playRockPaperScissors();

            String finalAttacker = playMukJjiPpa(attacker);
            String playerFinalChoice = playerCurrentChoice;
            String computerFinalChoice = computerCurrentChoice;
            String winner;

            if (finalAttacker.equals("PLAYER")) {
                playerWins++;
                winner = "WIN";
            } else {
                computerWins++;
                winner = "LOSE";
            }

            results.add(new RoundResult(round, playerFinalChoice, computerFinalChoice, winner));
            System.out.println("라운드 " + round + " 종료: 플레이어(" + translate(playerFinalChoice) +
                    ") vs 컴퓨터(" + translate(computerFinalChoice) + ") => " + winner);
        }

        printGameSummary(results, playerWins, computerWins);
    }

    private int readValidInt() {
        while (true) {
            String input = scanner.nextLine();
            try {
                int val = Integer.parseInt(input);
                if (val > 0) return val;
                else System.out.print("1 이상의 정수를 입력하세요: ");
            } catch (NumberFormatException e) {
                System.out.print("정수를 입력하세요: ");
            }
        }
    }

    private String readValidChoice() {
        while (true) {
            System.out.print("묵, 찌, 빠 중 하나를 입력하세요: ");
            String input = scanner.nextLine().trim();
            for (String choice : choices) {
                if (choice.equals(input)) return input;
            }
            System.out.println("잘못된 입력입니다. (묵, 찌, 빠)");
        }
    }

    private String playerCurrentChoice;
    private String computerCurrentChoice;

    private String playRockPaperScissors() {
        while (true) {
            System.out.println("가위바위보!!");
            playerCurrentChoice = readValidChoice();
            computerCurrentChoice = randomChoice();
            if (playerCurrentChoice.equals(computerCurrentChoice)) {
                System.out.println("무승부입니다. 다시 시도하세요.");
            } else if (beats.get(playerCurrentChoice).equals(computerCurrentChoice)) {
                System.out.println("공격자: PLAYER");
                return "PLAYER";
            } else {
                System.out.println("공격자: COMPUTER");
                return "COMPUTER";
            }
        }
    }

    private String playMukJjiPpa(String attacker) {
        while (true) {
            System.out.println("묵찌빠!!");
            playerCurrentChoice = readValidChoice();
            computerCurrentChoice = randomChoice();
            if (playerCurrentChoice.equals(computerCurrentChoice)) {
                System.out.println("무승부입니다. 현재 공격자: " + attacker);
                return attacker;
            }

            if (beats.get(playerCurrentChoice).equals(computerCurrentChoice)) {
                attacker = "PLAYER";
                System.out.println("공격자: PLAYER");
            } else {
                attacker = "COMPUTER";
                System.out.println("공격자: COMPUTER");
            }
        }
    }

    private String randomChoice() {
        return choices[new Random().nextInt(choices.length)];
    }

    private String translate(String korean) {
        return switch (korean) {
            case "묵" -> "ROCK";
            case "찌" -> "SCISSORS";
            case "빠" -> "PAPER";
            default -> "UNKNOWN";
        };
    }

    private void printGameSummary(List<RoundResult> results, int playerWins, int computerWins) {
        System.out.println("\n=== 전체 게임 결과 ===");
        for (RoundResult r : results) {
            System.out.printf("라운드 %d: 플레이어(%s) - 컴퓨터(%s) => %s\n",
                    r.roundNumber,
                    translate(r.playerChoice),
                    translate(r.computerChoice),
                    r.result
            );
        }

        System.out.println();
        if (playerWins > computerWins) {
            System.out.println("최종 승자: Player");
        } else if (playerWins < computerWins) {
            System.out.println("최종 승자: Computer");
        } else {
            System.out.println("최종 결과: 무승부");
        }
    }
}
