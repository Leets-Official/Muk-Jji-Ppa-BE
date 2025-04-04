package com.example.muk_jji_ppa.controller;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class MukJjiPpaController implements CommandLineRunner {

    private static final String[] CHOICES = {"묵", "찌", "빠"};
    private static final Map<String, String> WINNING_MOVES = Map.of( //Map.of 를 사용해서 간편하게 이기는 경우의 수를 정의함.
            "묵", "빠",
            "찌", "묵",
            "빠", "찌"
    );
    /* 위 맵의 경우 다음과 같이 정의할 수도 있음.
    private static final Map<String, String> WINNING_MOVES = new HashMap<>();
    static {
        WINNING_MOVES.put("묵", "빠");
        WINNING_MOVES.put("찌", "묵");
        WINNING_MOVES.put("빠", "찌");
    }
    */

    private String playerChoice, computerChoice;
    private int round; // 전체 라운드 수
    private int currentRound = 1; // 현재 라운드
    private int winner = -1; // 승자 미정: -1, 플레이어 공격: 0, 컴퓨터 공격: 1
    private int[] wins = {0, 0}; // wins[0]: 플레이어 승리 횟수, wins[1]: 컴퓨터 승리 횟수
    private List<String> roundsHistory = new ArrayList<>(); // 라운드별 기록 저장

    @Override
    public void run(String... args) {
        Scanner input = new Scanner(System.in);
        Random random = new Random();

        //라운드 수 입력
        round = roundSelection(input);
        //게임 실행
        playRounds(input, random);
        //최종 게임 결과 출력
        printResult();
        return;
    }

    // 라운드 수 결정
    private int roundSelection(Scanner input){
        System.out.println("총 게임 라운드 수를 입력하세요: ");
        int round;
        while(true) {
            try{
                round = Integer.parseInt(input.nextLine());

                //입력값이 양수가 아닌경우
                if (round <= 0) {
                    System.out.println("입력값은 양수여야 합니다.");
                    continue;
                }

                //정상인 경우
                System.out.printf("총 %d 라운드를 진행합니다.%n", round);
                return round;
            }
            //입력값이 정수가 아닌경우
            catch (NumberFormatException e){
                System.out.println("입력값은 정수여야 합니다.");
            }
        }
    }

    //게임 플레이
    private void playRounds(Scanner input, Random random){
        //라운드 진행
        while(currentRound <= round){
            System.out.printf("=== 라운드 %d 시작 ===%n", currentRound);
            System.out.println("가위바위보!!");

            //선제공격자 결정
            setWinner(input, random);
            //묵찌빠 시작
            MJB(input, random);
        }
    }

    //가위바위보로 승자 결정
    private void setWinner(Scanner input, Random random){
        winner = -1;
        while(true){
            makeChoices(input, random);
            if (WINNING_MOVES.get(playerChoice).equals(computerChoice)) {
                winner = 0; // 플레이어 승리
                System.out.println("공격자: PLAYER");
                break;
            } else if (WINNING_MOVES.get(computerChoice).equals(playerChoice)) {
                winner = 1; // 컴퓨터 승리
                System.out.println("공격자: COMPUTER");
                break;
            } else {
                System.out.println("무승부입니다. 다시 시도하세요.");
            }
        }
    }

    //묵찌빠 게임
    private void MJB(Scanner input, Random random){
    	System.out.println("묵찌빠!!");
        while(true){
        	System.out.println("묵, 찌, 빠 중 하나를 입력하세요:");
            makeChoices(input, random);
            if (WINNING_MOVES.get(playerChoice).equals(computerChoice)) {
                winner = 0; // 플레이어 공격
                System.out.println("공격자: PLAYER");
            } else if (WINNING_MOVES.get(computerChoice).equals(playerChoice)) {
                winner = 1; // 컴퓨터 공격
                System.out.println("공격자: COMPUTER");
            } else { // 현재 공격자 승리
                wins[winner]++;              
                System.out.println(String.format("무승부입니다. 현재 공격자: %s", (winner == 0 ? "PLAYER": "COMPUTER")));
                //현재 라운드 기록
                String currentRoundHistory = String.format("라운드 %d 종료: 플레이어(%s) vs 컴퓨터(%s) => %s",
                        currentRound, playerChoice, computerChoice, (winner == 0 ? "WIN" : "LOSE"));
                //현재 라운드 기록 출력
                System.out.println(currentRoundHistory);
                //현재 라운드 기록 저장
                roundsHistory.add(currentRoundHistory);
                currentRound++;
                break;
            }

        }
    }

    //플레이어 및 컴퓨터 묵,찌,빠 선택
    private void makeChoices(Scanner input, Random random){
        while(true){
            playerChoice = input.nextLine().trim();
            if (Arrays.asList(CHOICES).contains(playerChoice)) {
                break; // 올바른 입력이면 루프 종료
            }
            System.out.println("잘못된 입력입니다. (묵, 찌, 빠)");
        }
        // 컴퓨터 선택
        computerChoice = CHOICES[random.nextInt(3)];
    }
    
    private void printResult(){
        System.out.println("\n=== 모든 라운드 종료 ===");
        for (String record : roundsHistory) {
            System.out.println(record);
        }

        System.out.printf("최종 승자: %s%n", (wins[0] > wins[1] ? "Player" : "Computer"));
    }

}
