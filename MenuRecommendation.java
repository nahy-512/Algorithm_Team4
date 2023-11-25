/*
 * 
 * 사용자에게 선호하는 재료를 입력 받아 그에 맞는 음식을 추천하는 프로그램
 * 
 *  1) 사용자에게 선호하는 재료를 입력 받기
 *	2) 각 레시피에 대해 사용자의 선호 재료에 대한 점수를 계산하고 최종 점수를 기반으로 음식을 추천
 *
 *	- 음식 추천은 사용자가 선호하는 재료가 사용된 레시피에만 가중치를 부여하며, 사용자가 아무런 재료를 입력하지 않을 경우 모든 레시피를 대상으로 추천
 *	- 양과 유통기한을 고려하여 각 재료에 대한 가중치를 계산
 */

package algorithm;
import java.util.*;

// 재료 클래스
class Ingredient {
    String name;
    int amount;
    int daysToExpiration;

    // 재료 생성자
    Ingredient(String name, int amount, int daysToExpiration) {
        this.name = name;
        this.amount = amount;
        this.daysToExpiration = daysToExpiration;
    }
}

// 레시피 클래스
class Recipe {
    String name;
    String[] ingredients;

    // 레시피 생성자
    Recipe(String name, String[] ingredients) {
        this.name = name;
        this.ingredients = ingredients;
    }
}

// 메뉴 추천 클래스
public class MenuRecommendation {

    private static Set<String> userPreferences;   // 사용자의 선호 재료 목록을 저장할 Set

    public static void main(String[] args) {
        initializeUserPreferences(); // 사용자의 선호 재료 목록 초기화

        // 음식 재료 초기화
        Ingredient[] ingredients = {
                new Ingredient("고기", 500, 5),  //500g 양과 유통기한 5일
                new Ingredient("채소", 400, 3),
                new Ingredient("소스", 300, 10),
                new Ingredient("밥", 500, 11),
                new Ingredient("면", 500, 10),
                new Ingredient("빵", 400, 2),
                new Ingredient("소고기", 200, 3)
        };

        // 음식 레시피 초기화
        Recipe[] recipes = {
                new Recipe("비빔밥", new String[]{"밥", "고기", "채소", "소스"}),
                new Recipe("짜장면", new String[]{"면", "고기", "채소", "소스"}),
                new Recipe("스테이크", new String[]{"소고기", "채소", "소스"}),
                new Recipe("스파게티", new String[]{"면", "소스"}),
                new Recipe("샌드위치", new String[]{"빵", "고기", "채소"}),
        };

        // 사용자에게 선호하는 재료 입력 받기
        Scanner scanner = new Scanner(System.in);
        System.out.println("선호하는 재료를 입력하세요. (예: 고기, 면)");
        System.out.println("입력을 마치려면 엔터키");

        while (true) {
            String input = scanner.nextLine();
            if (input.isEmpty()) {
                break;
            }

            userPreferences.add(input);
        }

        // 메뉴 추천 및 결과 출력
        System.out.println("추천 음식: " + recommendMenu(ingredients, recipes));
    }

    // 사용자의 선호 재료 목록 초기화
    private static void initializeUserPreferences() {
        userPreferences = new HashSet<>();
    }

    // 메뉴 추천 함수
    private static String recommendMenu(Ingredient[] ingredients, Recipe[] recipes) {
        String recommendedMenu = "";
        double bestScore = -1;

        // 각 레시피에 대해 점수를 계산하고 최고 점수를 가진 음식을 추천
        for (Recipe recipe : recipes) {
            double score = 0;

            // 현재 레시피에 필요한 각 재료에 대해 점수를 계산하고 더함
            for (String ingredientName : recipe.ingredients) {
                if (userPreferences.contains(ingredientName)) {
                    // 사용자가 선호하는 재료에 해당하는 경우 가중치 부여
                    for (Ingredient ingredient : ingredients) {
                        if (ingredient.name.equals(ingredientName)) {
                            // 가중치 부여
                            score += calculateWeight(ingredient);
                        }
                    }
                }
            }

            // 사용자가 선호하는 재료가 있다면, 사용된 레시피에만 가중치를 부여 / 또는 아예 없을 때
            if (score > 0 || userPreferences.isEmpty()) {
                // 사용자가 선호하는 재료가 사용된 경우에만 최종 점수를 계산
                score /= recipe.ingredients.length;   // 재료 수로 정규화

                // 최고 점수보다 현재 레시피의 점수가 높으면 갱신
                if (score > bestScore) {
                    bestScore = score;
                    recommendedMenu = recipe.name;
                }
            }
        }

        return recommendedMenu;
    }

    // 양과 유통기한을 고려한 가중치 계산 함수
    private static double calculateWeight(Ingredient ingredient) {
        double amountWeight = ingredient.amount / 100.0;   // 양이 많을수록 긍정적 가중치
        double expirationWeight = (100 - ingredient.daysToExpiration) / 100.0;   // 유통기한이 짧을수록 긍정적 가중치

        // 양과 유통기한을 종합한 최종 가중치
        return amountWeight * expirationWeight;
    }
}