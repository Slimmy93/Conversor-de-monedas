package com.aluracursos.challengeconversor;

import com.aluracursos.herramientas.API;
import com.aluracursos.herramientas.Divisa;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Currency;
import java.util.Date;
import java.util.Scanner;
import java.util.Set;




public class PrincipalDivisas {
    public static String divisaInicial;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        double cantidadDivisa;
        String divisaAConvertir;
        Gson gson = new Gson();

        Set<Currency> listaDeDivisas = Currency.getAvailableCurrencies();
        outerLoop:
        while (true) {
            System.out.println("""
                    *************************************************
                    
                    ¡Sea bienvendid@ al Conversor de Divisas!
                    Instrucciones:
                    1.- Ingrese el codigo de divisa de origen.
                    2.- Ingrese el codigo de la divisa al que se va a convertir la divisa de origen.
                    3.- Ingrese la cantidad a convertir.
                    
                    ****  Codigos de divisas  ****
                    
                    ARS → Peso argentino
                    BOB → Peso boliviano
                    BRL → Real brasileño
                    CLP → Peso chileno
                    COP → Peso colombiano
                    EUR → Euro
                    GBP → Libra esterlina
                    JPY → Yen japones
                    MXN → Peso mexicano
                    USD → Dolar estadounidense
                    UYU → Peso uruguayo
                    
                    → (Si desea finalizar el conversor, digite: "Salir" en cualquier momento)
                    
                    *************************************************
                    
                    Ingrese el codigo de divisa de origen:
                    """);
            while (true) {
                divisaInicial = scanner.nextLine().toUpperCase();
                String divisaABuscar = divisaInicial;
                if (listaDeDivisas.stream().anyMatch(currency -> currency.getCurrencyCode().equals(divisaABuscar))) {
                    break;
                } else if (divisaInicial.equalsIgnoreCase("Salir")) {
                    System.out.println("Programa finalizado. ¡Gracias por usar el conversor!");
                    break outerLoop;
                } else {
                    System.out.println("Ingrese un codigo de divisa valido");
                }
            }
            while (true) {
                System.out.println("Ingrese el codigo de la divisa a la que se va a convertir la divisa de origen:");
                divisaAConvertir = scanner.nextLine().toUpperCase();
                String divisaAConvertirUsuario = divisaAConvertir;
                if (listaDeDivisas.stream().anyMatch(currency -> currency.getCurrencyCode().equals(divisaAConvertirUsuario))) {
                    break;
                } else if (divisaAConvertir.equalsIgnoreCase("Salir")) {
                    System.out.println("Programa finalizado. ¡Gracias por usar el conversor de monedas!");
                    break outerLoop;
                } else {
                    System.out.println("Ingrese un codigo de divisa valido");
                }
            }
            while (true) {
                try {
                    System.out.println("Ingrese la cantidad de " + divisaInicial + " que desea convertir a " + divisaAConvertir + " :");
                    String cantidadUsuario = scanner.nextLine();
                    cantidadDivisa = Double.valueOf(cantidadUsuario);
                    if (cantidadUsuario.equalsIgnoreCase("Salir")) {
                        System.out.println("Programa finalizado. ¡Gracias por usar el conversor de monedas!");
                        break outerLoop;
                    }else {
                        cantidadDivisa = Double.valueOf(cantidadUsuario);
                    }
                    if (cantidadDivisa > -99999) {
                        break;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Ingrese un numero");

                }
            }
            API api = new API();
            String resultado = api.API();

            try {
                Divisa divisa = gson.fromJson(resultado, Divisa.class);
                double tipoDeCambio = divisa.conversion_rates().get(divisaAConvertir);
                double conversion = cantidadDivisa * tipoDeCambio;
                DecimalFormat formatoDecimal = new DecimalFormat("#,##0.00");
                String conversionDecimal = formatoDecimal.format(conversion);
                System.out.println("""
                        \n*************************************************\s
                        
                        \nCantidad preliminar :\s""" + cantidadDivisa + " " + divisaInicial + """
                        \nTipo de Cambio: \s""" + tipoDeCambio + " " + divisaAConvertir + "/" + divisaInicial + """
                        \nResultado de la operacion:\s""" + conversionDecimal + " " + divisaAConvertir + """
                        
                        \n*************************************************\s
                        """);
                try {
                    File file = new File("Registro-de-transacciones.txt");
                    Date fecha = new Date();
                    SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
                    String fechaConFormato = formatoFecha.format(fecha);
                    FileWriter fileWriter = new FileWriter(file, true);
                    fileWriter.write("""
                            \nFecha de la operacion:\s""" + fechaConFormato + """
                            \nCantidad preliminar :\s""" + cantidadDivisa + " " + divisaInicial + """
                            \nTipo de Cambio: \s""" + tipoDeCambio + " " + divisaAConvertir + "/" + divisaInicial + """
                            \nResultado de la operacion:\s""" + conversionDecimal + " " + divisaAConvertir + """
                            \n**********\s
                            """);
                    fileWriter.close();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }

                System.out.println("""
                        ¿Desea hacer otra operacion?")
                        → Si
                        → No
                        """);

                String otraOperacion = scanner.nextLine().toLowerCase();
                while (true) {
                    if (otraOperacion.equals("no") || otraOperacion.equals("salir")) {
                        System.out.println("Programa finalizado, ¡gracias por usar el conversor de divisas!.");
                        break outerLoop;
                    } else if (otraOperacion.equals("si")) {
                        break;
                    } else {
                        System.out.println("Opcion invalida");
                        otraOperacion = scanner.nextLine().toLowerCase();
                    }
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
}

