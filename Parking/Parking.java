package com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Parking {
    private static volatile int parkingPlaces;
    private static volatile List<Ticket> ticketList;
    private static volatile Map<Ticket, Car> carsOnParking = new HashMap<>();
    private static int carCount = 1;

    private static void setParkingPlaces(BufferedReader reader) {
        while (true) {
            System.out.println("Введите количество машиномест: ");

            try {
                parkingPlaces = Integer.parseInt(reader.readLine());
                break;
            } catch (NumberFormatException e) {
                System.out.println("Введите верные данные!");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void generateTickets() {
        ticketList = new ArrayList<>(parkingPlaces);

        for (int i = 0; i < parkingPlaces; i++) {
            ticketList.add(i, new Ticket(i + 1));
        }
    }

    private void work(BufferedReader reader) {
        System.out.println("Список команд:\n" +
                "p:N - чтобы припарковать машину, где N - количество машин на въезд\n" +
                "u:N - чтобы выехать с парковки. N - номер парковочного билета\n" +
                "u:[1..n] - чтобы выехать с парковки нескольким машинам, где в квадратных скобках, через запятую передаются номера парковочных билетов\n" +
                "l - список машин, находящихся на парковке. Для каждой машины выводится ее порядковый номер и номер билета\n" +
                "c - количество оставшихся мест на парковке\n" +
                "e - выход из приложения\n" +
                "Введите команду: ");

        while (true) {
            try {
                String command = reader.readLine();
                String[] lines = new String[2];

                if (command.length() > 1)
                    lines = command.split(":");
                else
                    lines[0] = command;

                String[] finalLines;

                switch (lines[0]) {
                    case "p":
                        finalLines = lines;
                        new Thread(() -> park(finalLines[1])).start();
                        break;
                    case "u":
                        finalLines = lines;
                        if (finalLines[1].contains("["))
                            new Thread(() -> unPark(finalLines)).start();
                        else
                            new Thread(() -> unPark(finalLines[1])).start();
                        break;
                    case "l":
                        getListOfCars();
                        break;
                    case "c":
                        System.out.println("Количество свободных мест = " + parkingPlaces);
                        break;
                    case "e":
                        System.exit(0);
                    default:
                        System.out.println("Неправильный ввод данных!\n" +
                                "Введите команду: ");
                        break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private synchronized void park(String numberOfCars) {
        try {
            int number = Integer.parseInt(numberOfCars);
            Ticket ticket;

            while (number > 0) {
                if (parkingPlaces > 0) {
                    try {
                        this.wait(new Random().nextInt(4001) + 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    ticket = getTicket();
                    carsOnParking.put(ticket, new Car(carCount++));
                    parkingPlaces--;
                    number--;
                    System.out.println("Машина " + carsOnParking.get(ticket).getIndex() + " въехала на парковку.");
                } else {
                    try {
                        this.wait(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("Неправильный ввод данных!\n" +
                    "Введите команду: ");
        }
    }

    private Ticket getTicket() {
        Ticket ticket = null;
        for (Ticket t : ticketList) {
            if (t.isAvailable()) {
                ticket = t;
                ticket.setAvailability(false);
                break;
            }
        }
        return ticket;
    }

    private synchronized void unPark(String ticketNumber) {
        try {
            int number = Integer.parseInt(ticketNumber);

            try {
                this.wait(new Random().nextInt(4001) + 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Iterator<Map.Entry<Ticket, Car>> iterator = carsOnParking.entrySet().iterator();

            while (iterator.hasNext()) {
                Map.Entry<Ticket, Car> entry = iterator.next();
                if (entry.getKey().getId() == number) {
                    System.out.println("Машина " + entry.getValue().getIndex() + " выехала с парковки.");
                    entry.getKey().setAvailability(true);
                    iterator.remove();
                    parkingPlaces++;
                    break;
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("Неправильный ввод данных!\n" +
                    "Введите команду: ");
        }
    }

    private synchronized void unPark(String... tickets) {
        StringBuilder string = new StringBuilder(tickets[1]);

        string.deleteCharAt(0);
        string.deleteCharAt(string.length() - 1);
        String[] lines = string.toString().split(",");

        for (String s : lines) {
            try {
                int i = Integer.parseInt(s);
                if (i > ticketList.size())
                    throw new NumberFormatException();
            } catch (NumberFormatException e) {
                System.out.println("Неправильный ввод данных!\n" +
                        "Введите команду: ");
                return;
            }
        }

        for (int i = 0; i < lines.length; i++) {
            if (parkingPlaces == 1) {
                try {
                    this.wait(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            unPark(lines[i]);
        }
    }

    private void getListOfCars() {
        for (Map.Entry<Ticket, Car> pair : carsOnParking.entrySet()) {
            System.out.println("Порядковый номер: " + pair.getValue().getIndex() +
                    " Номер билета: " + pair.getKey().getId());
        }
    }

    public static void main(String[] args) {
        Parking myParking = new Parking();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        setParkingPlaces(reader);       //Установка размера парковки
        myParking.generateTickets();    //Определение количества парковочных билетов и присвоение им номеров
        myParking.work(reader);         //Основной алгоритм
    }
}
