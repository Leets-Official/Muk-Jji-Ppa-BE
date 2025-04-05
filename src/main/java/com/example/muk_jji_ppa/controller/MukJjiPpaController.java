package com.example.muk_jji_ppa.controller;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class MukJjiPpaController implements CommandLineRunner {

    private final Map<String, Integer> mukJjiPpa = new HashMap<>() { // 묵찌빠
        {
            put("묵", 0);
            put("찌", 1);
            put("빠", 2);
        }
    };
    private final String[] rockScissorsPaper = { "ROCK", "SCISSORS", "PAPER" };

    private final String[] winOrLoss = { "WIN", "LOSS" }; // WIN or LOSS

    private int round; // 진행할 라운드 수
    private List<String> roundResultMJP; // 종료된 묵찌빠
    private List<Integer> doesPlayerWin; // 플레이어가 이긴 라운드, 0이면 승리

    public MukJjiPpaController(List<String> roundResultMJP, List<Integer> doesPlayerWin) {
        this.roundResultMJP = roundResultMJP;
        this.doesPlayerWin = doesPlayerWin;
    }

    @Override
    public void run(String... args) {
        round = getRound();     // 라운드 입력
        playRound(round);       // 묵찌빠 진행
        printGameResult(round); // 최종 결과
    }

    // 최종 결과 메서드
    private void printGameResult(int round) {
        System.out.println("=== 전체 게임 결과 ===");

        long countPlayerWin = doesPlayerWin.stream().filter(n -> n == 0).count();
        long countComputerWin = round - countPlayerWin;

        for (int i = 0; i < round; i++) {
            System.out.println("라운드 " + (i + 1) + ": 플레이어(" + roundResultMJP.get(i) + ") - 컴퓨터(" + roundResultMJP.get(i) + ") => " + winOrLoss[doesPlayerWin.get(i)]);
        }

        if (countPlayerWin == countComputerWin) {
            System.out.println("\n최종 승자는 없습니다.(무승부)");
        } else {
            String winner = (countPlayerWin > countComputerWin) ? "Player" : "Computer";
            System.out.println("\n최종 승자: " + winner);
        }
    }

    // 라운드 진행 메서드
    public void playRound(int round) {
        for (int i = 0; i < round; i++) {
            System.out.println("=== 라운드 " + (i + 1) + " 시작 ===");

            // 가위바위보 진행
            System.out.println("가위바위보!!");
            String lastAttacker = playRockScissorsPaper();

            // 묵찌빠 진행
            System.out.println("묵찌빠!!");
            int computer = computerMJP();
            String lastResult = "mjp";

            while (true) {
                Integer player = inputMJP();
                String result = compareMJP(computer, player);

                if (!result.equals("무승부")) {
                    System.out.println("공격자: " + result);
                    lastAttacker = result;
                    computer = computerMJP();
                } else {
                    System.out.println("무승부입니다. 현재 공격자: " + lastAttacker);
                    lastResult = rockScissorsPaper[computer];
                    roundResultMJP.add(lastResult); // 라운드 결과에 저장
                    doesPlayerWin.add((lastAttacker.equals("COMPUTER")) ? 1 : 0); // 컴퓨터가 이겼을 경우 1, 사용자가 이겼을 경우 0
                    break;
                }
            }

            // 각 라운드 종료
            System.out.println("라운드 " + (i + 1) + " 종료: 플레이어(" + lastResult + ") vs 컴퓨터(" + lastResult + ") => " + winOrLoss[doesPlayerWin.get(i)] + "\n");
        }
    }

    // 가위바위보 진행 메서드
    public String playRockScissorsPaper() {
        int computer = computerMJP();
        String winner = "who?";

        while (true) {
            Integer player = inputMJP();
            winner = compareMJP(computer, player);

            if (winner.equals("무승부")) {
                System.out.println("무승부입니다. 다시 시도하세요.");
                computer = computerMJP();
            }
            else {
                System.out.println("공격자: " + winner);
                return winner;
            }
        }
    }

    // 묵찌빠 입력
    private Integer inputMJP() {
        while (true) {
            System.out.print("묵, 찌, 빠 중 하나를 입력하세요: ");
            String input = new Scanner(System.in).nextLine();

            if (!mukJjiPpa.containsKey(input)) { // 입력 예외 처리
                System.out.println("잘못된 입력입니다. (묵, 찌, 빠)");
            } else {
                return mukJjiPpa.get(input);
            }
        }
    }

    // winner: 컴퓨터 vs 사용자
    private String compareMJP(int computer, int player) {
        int result = player - computer;

        if (result == 0) {
            return "무승부";
        } else {
            return (result == 1 || result == -2) ? "COMPUTER" : "PLAYER";
        }
    }

    // 컴퓨터의 랜덤 묵찌빠
    public int computerMJP() {
        return new Random().nextInt(3);
    }

    // 라운드 수 정하기
    private int getRound() {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.print("총 게임 라운드 수를 입력하세요: ");

            try {
                int round = sc.nextInt();

                if (round < 1) {
                    System.out.println("최소 1라운드 이상 진행되어야 합니다.");
                } else {
                    System.out.println("총 " + round + " 라운드를 진행합니다.\n");
                    return round;
                }
            } catch (InputMismatchException e) {
                System.out.println("잘못된 입력입니다. (1 이상의 정수)");
                sc.nextLine();
            }
        }
    }
}
