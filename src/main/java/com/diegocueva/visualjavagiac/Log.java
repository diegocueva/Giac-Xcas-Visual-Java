package com.diegocueva.visualjavagiac;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author diegocueva.com
 */
public class Log {

    /**
     * Category con el que se inicializa el módulo
     */
    public static Logger CATEGORY;

    /**
     * Categori abreviado para mostrar en los logs
     */
    private static String CATEGORY_AVR;

    /**
     * Para almacenar el sessionId
     */
    private static final ThreadLocal<String> THREAD_LOCAL = new ThreadLocal<>();

    /**
     * Se invoca cuando se inicia un módulo. Se configura con el código del
     * módulo
     *
     * @param categoryName Código del módulo
     */
    public static void init(String categoryName) {
        CATEGORY = LoggerFactory.getLogger(categoryName);
        CATEGORY_AVR = categoryName;
        Log.info("LOG CATEGORY init by: " + categoryName);
    }

    /**
     * Log DEBUG
     *
     * @param message a mostrar en el log DEBUG
     */
    public static void debug(Object message) {
        String line = "|" + message;
        if (CATEGORY != null) {
            CATEGORY.info(line);
        }
    }

    /**
     * Log INFO
     *
     * @param message a mostrar en el log INFO
     */
    public static void info(Object message) {
        String line = "|" + message;
        if (CATEGORY != null) {
            CATEGORY.info(line);
        }
    }

    /**
     * Log ERROR
     *
     * @param message a mostrar en el log ERROR
     */
    public static void error(Object message) {
        String line = "|" + message;
        if (CATEGORY != null) {
            CATEGORY.error(line);
        }
    }

    /**
     * Log ERROR
     *
     * @param message a mostrar en el log ERROR
     * @param t excepción a mostrar
     */
    public static void error(Object message, Throwable t) {
        String line = "|" + message;
        if (CATEGORY != null) {
            CATEGORY.error(line, t);
        }
    }

    public static void setSessionId(String sessionId) {
        THREAD_LOCAL.set(sessionId);
    }
    
}
