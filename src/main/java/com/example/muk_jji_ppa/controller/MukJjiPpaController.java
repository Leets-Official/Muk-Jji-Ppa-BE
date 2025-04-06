package com.example.muk_jji_ppa.controller;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class MukJjiPpaController implements CommandLineRunner {

    @Override
    public void run(String... args) {
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();
        String[] choices = {"묵", "찌", "빠"};
        Map<String, String> winMap = Map.of("묵", "찌", "찌", "빠", "빠", "묵");

        int totalRounds = 0;
        while (true) {
            System.out.print("총 게임 라운드 수를 입력하세요: ");

            String input;
            try {
                input = scanner.nextLine();
            } catch (NoSuchElementException e) {
                System.out.println("입력을 받을 수 없습니다. 프로그램을 종료합니다.");
                return;
            }

            try {
                totalRounds = Integer.parseInt(input);
                if (totalRounds <= 0) throw new NumberFormatException();
                break;
            } catch (NumberFormatException e) {
                System.out.println("유효한 정수를 입력하세요.");
            }
        }

        System.out.println("총 " + totalRounds + " 라운드를 진행합니다.");

        int playerWins = 0, computerWins = 0;
        List<String> summary = new ArrayList<>();

        for (int round = 1; round <= totalRounds; round++) {
            System.out.println("\n=== 라운드 " + round + " 시작 ===");
            String attacker = null;
            String playerChoice = null, computerChoice = null;

            while (true) {
                System.out.println("가위바위보!!");
                playerChoice = getValidChoice(scanner);
                computerChoice = choices[random.nextInt(3)];
                if (playerChoice.equals(computerChoice)) {
                    System.out.println("무승부입니다. 다시 시도하세요.");
                    continue;
                }
                attacker = winMap.get(playerChoice).equals(computerChoice) ? "PLAYER" : "COMPUTER";
                System.out.println("공격자: " + attacker);
                break;
            }

            while (true) {
                System.out.println("묵찌빠!!");
                playerChoice = getValidChoice(scanner);
                computerChoice = choices[random.nextInt(3)];

                if (playerChoice.equals(computerChoice)) {
                    System.out.println("무승부입니다. 현재 공격자: " + attacker);
                    if ("PLAYER".equals(attacker)) playerWins++;
                    else computerWins++;

                    summary.add("라운드 " + round + ": 플레이어(" + translate(playerChoice) + ") - 컴퓨터(" + translate(computerChoice) + ") => " + (attacker.equals("PLAYER") ? "WIN" : "LOSE"));
                    System.out.println("=== 라운드 " + round + " 종료 ===");
                    System.out.println("라운드 " + round + " 종료: 플레이어(" + translate(playerChoice) + ") vs 컴퓨터(" + translate(computerChoice) + ") => " + (attacker.equals("PLAYER") ? "WIN" : "LOSE"));
                    break;
                }

                attacker = winMap.get(playerChoice).equals(computerChoice) ? "PLAYER" : "COMPUTER";
                System.out.println("공격자: " + attacker);
            }
        }

        System.out.println("\n=== 전체 게임 결과 ===");
        summary.forEach(System.out::println);

        String finalWinner = playerWins > computerWins ? "Player" : (computerWins > playerWins ? "Computer" : "Draw");
        System.out.println("\n최종 승자: " + finalWinner);
    }

    private String getValidChoice(Scanner scanner) {
        while (true) {
            System.out.print("묵, 찌, 빠 중 하나를 입력하세요: ");
            String input;
            try {
                input = scanner.nextLine().trim();
            } catch (NoSuchElementException e) {
                System.out.println("입력을 받을 수 없어 기본값 '묵'으로 진행합니다.");
                return "묵";
            }
            if (input.equals("묵") || input.equals("찌") || input.equals("빠")) {
                return input;
            }
            System.out.println("잘못된 입력입니다. (묵, 찌, 빠)");
        }
    }

    private String translate(String choice) {
        switch (choice) {
            case "묵": return "rock";
            case "찌": return "scissors";
            case "빠": return "paper";
            default: return "";
        }
    }

}

