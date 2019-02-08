import java.util.Random;

/*
Алгоритмы-прямоугольники

1. Дан двумерный массив N*N, который содержит несколько прямоугольников.
2. Различные прямоугольники не соприкасаются и не накладываются.
3. Внутри прямоугольник весь заполнен 1.
4. В массиве:
4.1) a[i, j] = 1, если элемент (i, j) принадлежит какому-либо прямоугольнику
4.2) a[i, j] = 0, в противном случае
5. getRectangleCount должен возвращать количество прямоугольников.

*/
public class Solution {
    private static int count = 0;

    public static void main(String[] args) {
        int c = 10;
        while (c > 0) {
            byte N = Byte.parseByte(String.valueOf(new Random().nextInt(11)));      //Размер матрицы
            byte[][] randomMatrix = new byte[N][N];

            for (int i = 0; i < randomMatrix.length; i++) {                     //Генерация содержимого и вывод в консоль
                for (int j = 0; j < randomMatrix[0].length; j++) {
                    if (new Random().nextBoolean())
                        randomMatrix[i][j] = 1;
                    else
                        randomMatrix[i][j] = 0;
                    System.out.print(randomMatrix[i][j] + " ");
                }
                System.out.println();
            }
            System.out.println();

            int count3 = getRectangleCount(randomMatrix);                       //Поиск прямоугольников согласно условию и вывод в консоль
            for (int i = 0; i < randomMatrix.length; i++) {
                for (int j = 0; j < randomMatrix[0].length; j++) {
                    System.out.print(randomMatrix[i][j] + " ");
                }
                System.out.println();
            }
            System.out.println();
            System.out.println("count = " + count3 + "\n");
            c--;
        }
    }

    public static int getRectangleCount(byte[][] a) {                           //Метод прохода по матрице
        int myCount;
        byte[] array;
        for (byte i = 0; i < a.length; i++) {
            for (byte j = 0; j < a[0].length; j++) {
                if (a[i][j] == 1) {
                    array = finder(i, j, a);
                    i = array[0];
                    j = array[1];
                }
            }
        }
        myCount = count;
        count = 0;
        return myCount;
    }

    private static byte[] finder(byte i, byte j, byte[][] a) {                  //Метод поиска прямоугольников в матрице
        byte[] array = new byte[2];
        Point upperLeft = new Point(i, j);
        Point upperRight = stringFinder(i, j, a);
        Point lowerLeft = columnFinder(i, j, a);
        Point lowerRight = stringFinder(lowerLeft.i, lowerLeft.j, a);

        if (upperLeft.equals(upperRight) && upperLeft.equals(lowerLeft)) {
            a[i][j] = 2;
            if (checkBorders(upperLeft, lowerRight, a)) {
                count++;
                a[i][j] = 4;
            }
        } else if (upperRight.j == lowerRight.j) {
            if (checkRectangle(upperLeft, upperRight, lowerLeft, a)) {
                paintRectangle(upperLeft, upperRight, lowerLeft, a, (byte) 2);
                if (checkBorders(upperLeft, lowerRight, a)) {
                    count++;
                    paintRectangle(upperLeft, upperRight, lowerLeft, a, (byte) 4);
                }
            }
        } else {
            if (upperRight.j > lowerRight.j)
                paintRectangle(upperLeft, upperRight, lowerLeft, a, (byte) 2);
            else
                paintRectangle(upperLeft, lowerRight, lowerLeft, a, (byte) 2);
        }
        array[0] = upperRight.i;
        array[1] = upperRight.j;
        return array;
    }

    private static Point stringFinder(byte iStart, byte jStart, byte[][] a) {       //Поиск правой точки прямоугольника
        for (byte j = jStart; j < a[iStart].length; j++) {
            if (a[iStart][j] == 0) {
                return new Point(iStart, (byte) (j - 1));
            }
        }
        return new Point(iStart, (byte) (a[iStart].length - 1));
    }

    private static Point columnFinder(byte iStart, byte jStart, byte[][] a) {       //Поиск нижней точки прямоугольника
        for (byte i = iStart; i < a.length; i++) {
            if (a[i][jStart] == 0) {
                return new Point((byte) (i - 1), jStart);
            }
        }
        return new Point((byte) (a.length - 1), jStart);
    }

    private static boolean checkRectangle(Point upperLeft, Point upperRight, Point lowerLeft, byte[][] a) {     //Проверка содержимого прямоугольника
        for (byte i = upperLeft.i; i <= lowerLeft.i; i++) {
            for (byte j = upperLeft.j; j <= upperRight.j; j++) {
                if (a[i][j] != 1)
                    return false;
            }
        }
        return true;
    }

    private static void paintRectangle(Point upperLeft, Point right, Point lowerLeft, byte[][] a, byte paint) {     //Закраска прямоугольника
        for (byte i = upperLeft.i; i <= lowerLeft.i; i++) {
            for (byte j = upperLeft.j; j <= right.j; j++) {
                if (a[i][j] == 1 || a[i][j] == 2)
                    a[i][j] = paint;
            }
        }
    }

    private static boolean checkBorders(Point upperLeft, Point lowerRight, byte[][] a) {                        //Проверка границ прямоугольника
        if (upperLeft.i - 1 < 0) {                                                      //Верхняя
            if (upperLeft.j - 1 < 0) {                                                  //Верхняя + Левая
                if (lowerRight.i + 1 == a.length) {                                     //Верхняя + Левая + Нижняя
                    if (lowerRight.j + 1 == a[0].length) {                              //Верхняя + Левая + Нижняя + Правая
                        return true;
                    } else {                                                            //Верхняя + Левая + Нижняя
                        return isBorderCollision(upperLeft.i, lowerRight.i, (byte) (lowerRight.j + 1), (byte) (lowerRight.j + 1), a);                  //Правая
                    }
                } else if (lowerRight.j + 1 == a[0].length) {                           //Верхняя + Левая + Правая
                    return isBorderCollision((byte) (lowerRight.i + 1), (byte) (lowerRight.i + 1), upperLeft.j, lowerRight.j, a);                      //Нижняя
                } else {                                                                //Верхняя + Левая
                    if (isBorderCollision(upperLeft.i, (byte) (lowerRight.i + 1), (byte) (lowerRight.j + 1), (byte) (lowerRight.j + 1), a))            //Правая
                        return isBorderCollision((byte) (lowerRight.i + 1), (byte) (lowerRight.i + 1), upperLeft.j, (byte) (lowerRight.j + 1), a);     //Нижняя
                }
            } else if (lowerRight.i + 1 == a.length) {                                  //Верхняя + Нижняя
                if (lowerRight.j + 1 == a[0].length) {                                  //Верхняя + Нижняя + Правая
                    return isBorderCollision(upperLeft.i, lowerRight.i, (byte) (upperLeft.j - 1), (byte) (upperLeft.j - 1), a);                        //Левая
                } else {                                                                //Верхняя + Нижняя
                    if (isBorderCollision(upperLeft.i, lowerRight.i, (byte) (upperLeft.j - 1), (byte) (upperLeft.j - 1), a))                           //Левая
                        return isBorderCollision(upperLeft.i, lowerRight.i, (byte) (lowerRight.j + 1), (byte) (lowerRight.j + 1), a);                  //Правая
                }
            } else if (lowerRight.j + 1 == a[0].length) {                               //Верхняя + Правая
                if (isBorderCollision(upperLeft.i, (byte) (lowerRight.i + 1), (byte) (upperLeft.j - 1), (byte) (upperLeft.j - 1), a))                  //Левая
                    return isBorderCollision((byte) (lowerRight.i + 1), (byte) (lowerRight.i + 1), (byte) (upperLeft.j - 1), lowerRight.j, a);         //Нижняя
            } else {                                                                    //Верхняя
                if (isBorderCollision(upperLeft.i, (byte) (lowerRight.i + 1), (byte) (lowerRight.j + 1), (byte) (lowerRight.j + 1), a))                //Правая
                                                                                                                                                        //Нижняя
                    if (isBorderCollision((byte) (lowerRight.i + 1), (byte) (lowerRight.i + 1), (byte) (upperLeft.j - 1), (byte) (lowerRight.j + 1), a))
                        return isBorderCollision(upperLeft.i, (byte) (lowerRight.i + 1), (byte) (upperLeft.j - 1), (byte) (upperLeft.j - 1), a);       //Левая
            }
        } else if (upperLeft.j - 1 < 0) {                                               //Левая
            if (lowerRight.i + 1 == a.length) {                                         //Левая + Нижняя
                if (lowerRight.j + 1 == a[0].length) {                                  //Левая + Нижняя + Правая
                    return isBorderCollision((byte) (upperLeft.i - 1), (byte) (upperLeft.i - 1), upperLeft.j, lowerRight.j, a);                        //Верхняя
                } else {                                                                //Левая + Нижняя
                    if (isBorderCollision((byte) (upperLeft.i - 1), (byte) (upperLeft.i - 1), upperLeft.j, (byte) (lowerRight.j + 1), a))              //Верхняя
                        return isBorderCollision((byte) (upperLeft.i - 1), lowerRight.i, (byte) (lowerRight.j + 1), (byte) (lowerRight.j + 1), a);     //Правая
                }
            } else if (lowerRight.j + 1 == a[0].length) {                               //Левая + Правая
                if (isBorderCollision((byte) (upperLeft.i - 1), (byte) (upperLeft.i - 1), upperLeft.j, lowerRight.j, a))                               //Верхняя
                    return isBorderCollision((byte) (lowerRight.i + 1), (byte) (lowerRight.i + 1), upperLeft.j, lowerRight.j, a);                      //Нижняя
            } else {                                                                    //Левая
                if (isBorderCollision((byte) (upperLeft.i - 1), (byte) (upperLeft.i - 1), upperLeft.j, (byte) (lowerRight.j + 1), a))                  //Верхняя
                                                                                                                                                        //Правая
                    if (isBorderCollision((byte) (upperLeft.i - 1), (byte) (lowerRight.i + 1), (byte) (lowerRight.j + 1), (byte) (lowerRight.j + 1), a))
                        return isBorderCollision((byte) (lowerRight.i + 1), (byte) (lowerRight.i + 1), upperLeft.j, (byte) (lowerRight.j + 1), a);     //Нижняя
            }
        } else if (lowerRight.i + 1 == a.length) {                                      //Нижняя
            if (lowerRight.j + 1 == a[0].length) {                                      //Нижняя + Правая
                if (isBorderCollision((byte) (upperLeft.i - 1), lowerRight.i, (byte) (upperLeft.j - 1), (byte) (upperLeft.j - 1), a))                  //Левая
                    return isBorderCollision((byte) (upperLeft.i - 1), (byte) (upperLeft.i - 1), (byte) (upperLeft.j - 1), lowerRight.j, a);           //Верхняя
            } else {                                                                    //Нижняя
                if (isBorderCollision((byte) (upperLeft.i - 1), lowerRight.i, (byte) (upperLeft.j - 1), (byte) (upperLeft.j - 1), a))                  //Левая
                    if (isBorderCollision((byte) (upperLeft.i - 1), (byte) (upperLeft.i - 1), (byte) (upperLeft.j - 1), (byte) (lowerRight.j + 1), a)) //Верхняя
                        return isBorderCollision((byte) (upperLeft.i - 1), lowerRight.i, (byte) (lowerRight.j + 1), (byte) (lowerRight.j + 1), a);     //Правая
            }
        } else if (lowerRight.j + 1 == a[0].length) {                                   //Правая
            if (isBorderCollision((byte) (lowerRight.i + 1), (byte) (lowerRight.i + 1), (byte) (upperLeft.j - 1), lowerRight.j, a))                    //Нижняя
                if (isBorderCollision((byte) (upperLeft.i - 1), (byte) (lowerRight.i + 1), (byte) (upperLeft.j - 1), (byte) (upperLeft.j - 1), a))     //Левая
                    return isBorderCollision((byte) (upperLeft.i - 1), (byte) (upperLeft.i - 1), (byte) (upperLeft.j - 1), lowerRight.j, a);           //Верхняя
        } else {
            if (isBorderCollision((byte) (upperLeft.i - 1), (byte) (lowerRight.i + 1), (byte) (lowerRight.j + 1), (byte) (lowerRight.j + 1), a))       //Правая
                if (isBorderCollision((byte) (lowerRight.i + 1), (byte) (lowerRight.i + 1), (byte) (upperLeft.j - 1), (byte) (lowerRight.j + 1), a))   //Нижняя
                    if (isBorderCollision((byte) (upperLeft.i - 1), (byte) (lowerRight.i + 1), (byte) (upperLeft.j - 1), (byte) (upperLeft.j - 1), a)) //Левая
                                                                                                                                                        //Верхняя
                        return isBorderCollision((byte) (upperLeft.i - 1), (byte) (upperLeft.i - 1), (byte) (upperLeft.j - 1), (byte) (lowerRight.j + 1), a);
        }
        return false;
    }

    private static boolean isBorderCollision(byte iStart, byte iEnd, byte jStart, byte jEnd, byte[][] a) {          //Проверка наличия соприкосновений
        for (byte i = iStart; i <= iEnd; i++) {
            for (byte j = jStart ; j <= jEnd; j++) {
                if (a[i][j] != 0)
                    return false;
            }
        }
        return true;
    }
}
