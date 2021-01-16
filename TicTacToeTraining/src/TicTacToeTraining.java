import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

class Point{
    int x, y;
    public Point(int x, int y){
        this.x = x;
        this.y = y;
    }
}

public class TicTacToeTraining {
    /**
     * Блок настроек игры
     */
    private static final char DOT_EMPTY = '*'; // Символ пустых ячеек
    private static final char DOT_X = 'X'; // Символ X
    private static final char DOT_O = 'O'; // Символ O
    private static char[][] map; // Игровое поле
    private static char[][] mapComputer; // Игровое для компьютера.
    private static final int SIZE = 3;  // Размер поля
    private static Scanner scanner = new Scanner(System.in);
    private static Random random = new Random();
    private static int choiceOfAI; // Выбор реализации ИИ.
    private static int choiceMove; // Выбор очередности хода.

    public static void main(String[] args) {
        System.out.println("Выберите варианты Искусственного Интеллекта:\n (1)ИИ в процедурном стиле.\n (2)ИИ с МиниМакс Альфа-Бета обрезкой Процедурный стиль.\n (3)ИИ с МиниМакс Альфа-Бета обрезкой ООП.");
        choiceOfAI = scanner.nextInt();

        initMap();

        System.out.println("Выберите кто будет ходить первым:\n (1) Компьютер.\n (2) Человек.");
        choiceMove = scanner.nextInt();

        if(choiceOfAI > 1 && choiceMove == 1){
            randomMove();
        } else if (choiceOfAI == 1 && choiceMove == 1) {
            int randomOrNot = random.nextInt(2);
            System.out.println(randomOrNot);
            if(randomOrNot == 1){
                randomMove();
            } else AIProceduralStyle();
        }
        printMap(map);

        while (true) {

            humanTurn(); // Ход человека
            if (isEndGame(DOT_X)) {
                break;
            }
            if(choiceOfAI == 1) {
                AIProceduralStyle();
                System.out.println("Процедурный ИИ сделал ход! Теперь Ваш ход!");
            }
            if(choiceOfAI == 2) {
                toMiniMaxAlphaBeta(0, DOT_O);
                toComputersMove(computersMoveX, computersMoveY, DOT_O);
                System.out.println("ИИ МиниМакс Альфа-Бета в процедурном стиле сделал ход! Теперь Ваш ход!");
            }
            if(choiceOfAI == 3) {
                miniMaxAlphaBeta(0, DOT_O);
                placeAMove(computersMove, DOT_O);
                System.out.println("ИИ МиниМакс Альфа-Бета в ООП стиле сделал ход! Теперь Ваш ход!");
            }
            if (isEndGame(DOT_O)) {
                break;
            }
        }
        System.out.println("Игра закончена!");
    }

    /**
     * Метод создания игрового поля
     */
    private static void initMap() {
        map = new char[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                map[i][j] = DOT_EMPTY;
            }
        }
    }

    /**
     * Метод вывода игрового поля на экран с текущими значениями
     */
    private static void printMap(char[][] arr) {
        for (int i = 0; i <= SIZE; i++) {
            System.out.print(i + " ");
        }
        System.out.println();
        for (int i = 0; i < SIZE; i++) {
            System.out.print((i + 1) + " ");
            for (int j = 0; j < SIZE; j++) {
                System.out.print(arr[i][j] + " ");
            }
            System.out.println();
        }
    }

    /**
     * Метод хода человека
     */
    private static void humanTurn() {
        int x, y;
        do {
            System.out.println("Ваш ход, введите координаты клетки через пробел от 1 до 3.");
            x = scanner.nextInt() - 1;
            y = scanner.nextInt() - 1;
        }
        while (!isCellValid(x, y, DOT_X));
        map[x][y] = DOT_X;
    }

    /**
     * Метод Процедурного ИИ
     */
    private static void AIProceduralStyle() {
        // Находим выйгрышный ход компьютера
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (map[i][j] == DOT_EMPTY) {
                    map[i][j] = DOT_O;
                    if (checkWin(DOT_O))
                        return;
                    if (!checkWin(DOT_O)) {
                        map[i][j] = DOT_EMPTY;
                    }
                }
            }
        }
        //Блокируем выигрышный ход человека
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (map[i][j] == DOT_EMPTY) {
                    map[i][j] = DOT_X;
                    if (checkWin(DOT_X)) {
                        map[i][j] = DOT_O;
                        return;
                    }
                    if (!checkWin(DOT_X)) {
                        map[i][j] = DOT_EMPTY;
                    }
                }
            }
        }
        //Если центральная клетка свободна мы её занимаем
        int center = (((SIZE + 1) / 2) - 1);
        if (map[center][center] == DOT_EMPTY) {
            map[center][center] = DOT_O;
            return;
        }
        //Занимает правую нижнюю клетку
        if(map[1][2] == DOT_X && map [2][1] == DOT_X && map[2][2] == DOT_EMPTY || map[1][2] == DOT_X && map[2][0] == DOT_X) {
            map[2][2] = DOT_O;
            System.out.println("Правая нижняя клетка");
            return;
        }
        //Занимаем левую нижнюю клетку
        if(map[1][0] == DOT_X && map[2][1] == DOT_X && map[2][0] == DOT_EMPTY || map[1][0] == DOT_X && map[2][2] == DOT_X && map[2][0] == DOT_EMPTY){
            map[2][0] = DOT_O;
            System.out.println("Левая нижняя клетка");
            return;
        }
        // Ход по диагоналям, если занята центральная клетка
        if(map[1][1] == DOT_X){
            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {
                    if (map[i][j] == DOT_EMPTY) {
                        map[i][j] = DOT_X;
                        if (checkDiagonalComputerTurn(DOT_X)) {
                            System.out.println("Диагонали X " + checkDiagonalComputerTurn(DOT_X));
                            map[i][j] = DOT_O;
                            return;
                        }
                        if (!checkDiagonalComputerTurn(DOT_X)) {
                            map[i][j] = DOT_EMPTY;
                        }
                    }
                }
            }
        }
        // Проверка на 2 шага вперёд по СТОЛБЦАМ, чтобы в столбце были: 1 клетка со знаком компьютера и 2 свободные
        if (map[0][2] == DOT_X || map[2][0] == DOT_X || map[0][0] == DOT_X || map[2][2] == DOT_X) {
            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {
                    if (map[i][j] == DOT_EMPTY) {
                        map[i][j] = DOT_O;
                        if (checkVerticalComputerTurn(DOT_O)) {
                            System.out.println("Столбцы O " + checkVerticalComputerTurn(DOT_O));
                            return;
                        }
                        if (!checkVerticalComputerTurn(DOT_O)) {
                            map[i][j] = DOT_EMPTY;
                        }
                    }
                }
            }
        }
        if(map[1][1] == DOT_O && map[0][0] != DOT_X && map[0][2] != DOT_X && map[2][0] != DOT_X && map[2][2] != DOT_X){
            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {
                    if (map[i][j] == DOT_EMPTY) {
                        map[i][j] = DOT_O;
                        if (checkDiagonalComputerTurn(DOT_O)) {
                            System.out.println("Диагонали O " + checkDiagonalComputerTurn(DOT_O));
                            return;
                        }
                        if (!checkDiagonalComputerTurn(DOT_O)) {
                            map[i][j] = DOT_EMPTY;
                        }
                    }
                }
            }
        }
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (map[i][j] == DOT_EMPTY) {
                    map[i][j] = DOT_O;
                    if (checkLineComputerTurn(DOT_O)) {
                        System.out.println("Строки O " + checkLineComputerTurn(DOT_O));
                        return;
                    }
                    if (!checkLineComputerTurn(DOT_O)) {
                        map[i][j] = DOT_EMPTY;
                    }
                }
            }
        }
        // рандомный ход
        randomMove();
        System.out.println("Рандомный ход");
    }

    /**
     * Проверка на валидность хода
     * @param x - координаты клетки поля: X
     * @param y - координаты клетки поля: Y
     * @return признак валидности хода
     */
    private static boolean isCellValid(int x, int y, char playerSymbol) {
        if (x < 0 || x >= SIZE || y < 0 || y >= SIZE) {
            if(playerSymbol != DOT_O)System.out.println("Вы ввели некорректные координаты клетки!");
            return false;
        }
        if (map[x][y] == DOT_EMPTY) return true;
        if(playerSymbol != DOT_O) System.out.println("Вы ввели координаты занятой клетки!");
        return false;
    }

    /**
     * Метод проверки игры на завершение
     * @param playerSymbol сомвол, которым играет текущий игрок
     * @return boolean - признак завершения игры
     */
    private static boolean isEndGame(char playerSymbol) {
        printMap(map);
        if (checkWin(playerSymbol)) {
            System.out.println("Победили " + playerSymbol + "!");
            return true;
        }
        if (isMapFull() && !checkWin(playerSymbol)) {
            System.out.println("Ничья!");
            return true;
        }
        return false;
    }

    /**
     * Проверка на 100%-ю заполненность поля
     * @return boolean признак наличия свободных ячеек
     */
    private static boolean isMapFull() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (map[i][j] == DOT_EMPTY)
                    return false;
            }
        }
        return true;
    }

    /**
     * Проверка выйгрышных комбинаций компьтера, или человека
     * @param playerSymbol символ игрока, или компьютера для проверки
     * @return true or false
     */
    private static boolean checkWin(char playerSymbol){
        boolean checkLine, checkColumns, checkDiagonalA, checkDiagonalB;
        for (int i = 0; i < map.length; i++){
            checkLine = true;
            checkColumns = true;
            checkDiagonalA = true;
            checkDiagonalB = true;
            for (int j = 0; j < map.length; j++){
                checkLine &= (map[i][j] == playerSymbol);
                checkColumns &= (map[j][i] == playerSymbol);
                checkDiagonalA &= (map[j][j] == playerSymbol);
                checkDiagonalB &= (map[map.length - 1 - j][j] == playerSymbol);
            }
            if (checkLine || checkColumns || checkDiagonalA || checkDiagonalB) return true;
        }
        return false;
    }

    private static boolean checkMoveProceduralAI(char playerSymbol, String choiceDirection){
        boolean checkLine, checkColumns, checkDiagonalA, checkDiagonalB;
        for (int i = 0; i < map.length; i++){
            checkLine = true;
            checkColumns = true;
            checkDiagonalA = true;
            checkDiagonalB = true;
            for (int j = 0; j < map.length; j++){
                checkLine &= (map[i][j] == playerSymbol);
                checkColumns &= (map[j][i] == playerSymbol);
                checkDiagonalA &= (map[j][j] == playerSymbol);
                checkDiagonalB &= (map[map.length - 1 - j][j] == playerSymbol);
            }
            if (choiceDirection.equals(checkLine) && checkLine) return true;
            if (choiceDirection.equals(checkColumns) && checkColumns) return true;
            if (choiceDirection.equals(checkDiagonalA) && checkDiagonalA) return true;
            if (choiceDirection.equals(checkDiagonalB) && checkDiagonalB) return true;
        }
        return false;
    }
    /**
     * Делаем проверку ближайших символов по вертикали для хода компьтера
     * @param playerSymbol Символ игрока.
     * @return результат проверки.
     */
    private static boolean checkVerticalComputerTurn(char playerSymbol) {
        int emptyCellVertical, singCellVertical;
        for (int x = 0; x < SIZE; x++) {
            emptyCellVertical = 0;
            singCellVertical = 0;
            for (int y = 0; y < SIZE; y++) {
                if (map[y][x] == playerSymbol)
                    singCellVertical++;
                else if (map[y][x] == DOT_EMPTY)
                    emptyCellVertical++;
                if ((singCellVertical == SIZE - 1) && (emptyCellVertical == SIZE - 2))
                    return true;

            }
        }
        return false;
    }
    /**
     * Делаем проверку ближайших символов по диагонали для хода компьтера
     * @param playerSymbol Символ игрока.
     * @return результат проверки.
     */
    private static boolean checkDiagonalComputerTurn(char playerSymbol) {
        int emptyCellDiagonalA = 0, singCellDiagonalA = 0, emptyCellDiagonalB = 0, singCellDiagonalB = 0;
        for (int x = 0; x < SIZE; x++) {
            if (map[x][x] == playerSymbol)
                singCellDiagonalA++;
            else if (map[x][x] == DOT_EMPTY)
                emptyCellDiagonalA++;
            if ((singCellDiagonalA == SIZE - 1) && (emptyCellDiagonalA == SIZE - 2))
                return true;
            if (map[x][SIZE - 1 - x] == playerSymbol)
                singCellDiagonalB++;
            else if (map[x][SIZE - 1 - x] == DOT_EMPTY)
                emptyCellDiagonalB++;
            if ((singCellDiagonalB == SIZE - 1) && (emptyCellDiagonalB == SIZE - 2))
                return true;
        }
        return false;
    }

    private static boolean checkLineComputerTurn(char playerSymbol){
        int emptyCellLine, singCellLine;
        for (int x = 0; x < SIZE; x++) {
            emptyCellLine = 0;
            singCellLine = 0;
            for (int y = 0; y < SIZE; y++) {
                if (map[x][y] == playerSymbol)
                    singCellLine++;
                else if (map[x][y] == DOT_EMPTY)
                    emptyCellLine++;
                if ((singCellLine == SIZE - 1) && (emptyCellLine == SIZE - 2))
                    return true;
            }
        }
        return false;
    }

    /**
     * Переменная принимающая список свободных клеток поля в методе getAvailableStates
     */
    static List<Point> availablePoint;

    /**
     * Метод получения доступных клеток поля
     * @return ArrayList availablePoint
     */
    private static List<Point> getAvailableStates(){
        availablePoint = new ArrayList<>();
        for (int i = 0; i < map.length; ++i){
            for (int j = 0; j < map.length; ++j){
                if (map[i][j] == DOT_EMPTY){
                    availablePoint.add(new Point(i,j));
                }
            }
        }
        return availablePoint;
    }

    /**
     * Метод хода Компьютера, в координаты полученные в методе miniMaxAlphaBeta
     * @param point координаты клетки полученные в методе miniMaxAlphaBeta
     * @param playerSymbol символ, который помещается в координаты клетки (ИИ / Человек)
     */
    private static void placeAMove(Point point, char playerSymbol){
        map[point.x][point.y] = playerSymbol;
    }

    /**
     * Переменная принимающая параметры: X и Y в методе miniMaxAlphaBeta
     */
    static Point computersMove;

    /**
     * Метод МиниМакс Альфа-Бета в ОПП стиле
     * @param depth параметр глубины хода
     * @param turn параметр поочередного хода (ИИ / Человек)
     * @return МиниМакс возвращает счет клетки, которую оценивает
     */
    private static int miniMaxAlphaBeta(int depth, char turn){
        if (checkWin(DOT_O)) return +1;
        if (checkWin(DOT_X)) return -1;
        List<Point> pointsAvailable = getAvailableStates();
        if (pointsAvailable.isEmpty()) return 0;
        int min = Integer.MAX_VALUE, max = Integer.MIN_VALUE;
        for (int i = 0; i < pointsAvailable.size(); ++i){
            Point point = pointsAvailable.get(i);
            if (turn == DOT_O){
                placeAMove(point, DOT_O);
                int currentScore = miniMaxAlphaBeta(+1, DOT_X);
                max = Math.max(currentScore, max);
                if (depth == 0) System.out.println("Счёт клетки: "+(i + 1)+" = "+currentScore);
                if (currentScore >= 0 && depth == 0){ computersMove = point;}
                if (currentScore == 1){ map[point.x][point.y] = DOT_EMPTY; break;}
                if (i == pointsAvailable.size() - 1 && max < 0 && depth == 0){ computersMove = point;}
            } else if (turn == DOT_X){
                placeAMove(point, DOT_X);
                int currentScore = miniMaxAlphaBeta(+1, DOT_O);
                min = Math.min(currentScore, min);
                if (min == - 1){ map[point.x][point.y] = DOT_EMPTY; break;}
            }
            map[point.x][point.y] = DOT_EMPTY;
        }
        return turn == DOT_O ? max : min;
    }

    /**
     * Координаты X и Y для совершения хода компьютера, которые определяются в методе toMiniMaxAlphaBeta
     */
    private static int computersMoveX; // Координаты X для хода компьютера, которые определяются в методе toMiniMaxAlphaBeta
    private static int computersMoveY; // Координаты Y для хода компьютера, которые определяются в методе toMiniMaxAlphaBeta

    /**
     * Метод хода компьютера для метода toMiniMaxAlphaBeta
     * @param x координата клетки X, которая определяется в методе toMiniMaxAlphaBeta
     * @param y координата клетки Y, которая определяется в методе toMiniMaxAlphaBeta
     * @param playerSymbol параметр принмает символ того игрока, который совершает ход
     */
    private static void toComputersMove(int x ,int y, char playerSymbol){
        map[x][y] = playerSymbol;
    }

    /**
     * Метод МиниМакс с Альфа-Бета обрезкой в процедурном стиле, реализован максимально доступно для понимания
     * @param depth параметр глубины рекурсии метода toMiniMaxAlphaBeta
     * @param turn параметр поочерёдности ходов
     * @return Метод возвращает счёт с проверяемой клетки поля
     */
    private static int toMiniMaxAlphaBeta(int depth, char turn){
        if (checkWin(DOT_O)) return +1;
        if (checkWin(DOT_X)) return -1;
        if (isMapFull()) return 0;
        int max = - 100000, min = 100000, bestMove = - 1;
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map.length; j++) {
                if (map[i][j] == DOT_EMPTY) {
                    if (turn == DOT_O) {
                        toComputersMove(i, j, DOT_O);
                        int currentScore = toMiniMaxAlphaBeta(+1, DOT_X);
                        if (currentScore > max) max = currentScore;
                        if (depth == 0)
                            System.out.println("Счёт клетки: [" + i + ", " + j + "]" + " = " + currentScore);
                        if (currentScore >= 0 && depth == 0) {
                            if (currentScore > bestMove) {
                                bestMove = currentScore;
                                computersMoveX = i;
                                computersMoveY = j;
                            }
                        }
                        if (currentScore == 1) {
                            map[i][j] = DOT_EMPTY;
                            i = map.length - 1;
                            break;
                        }
                        if (i == map.length - 1 && max < 0 && depth == 0) {
                            computersMoveX = i; computersMoveY = j;
                        }
                    } else if (turn == DOT_X) {
                        toComputersMove(i, j, DOT_X);
                        int currentScore = toMiniMaxAlphaBeta(+1, DOT_O);
                        if (currentScore < min) min = currentScore;
                        if (min == -1) {
                            map[i][j] = DOT_EMPTY;
                            break;
                        }
                    }
                    map[i][j] = DOT_EMPTY;
                }
            }
        }
        return turn == DOT_O ? max : min;
    }

    /**
     * Рандомный ход добавлен для разнообразия первого хода компьютера, если игрок выбрал, что первым ходит компьютер, МиниМакс также способен делать первый ход.
     */
    private static void randomMove(){
        int x, y;
        do {
            x = random.nextInt(3);
            y = random.nextInt(3);
        } while (!isCellValid(x, y, DOT_O));
        toComputersMove(x, y, DOT_O);
        System.out.println("Компьютер сделал ход! Теперь Ваш ход!");
    }
}