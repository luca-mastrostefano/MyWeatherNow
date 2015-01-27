package com.example.myweathernow.util;

import com.example.myweathernow.background_check.*;
import com.example.myweathernow.persistency.*;

import java.util.*;

/**
 * Created by ele on 27/01/15.
 */
public class PhraseMaker {
    private static Map<String, String> phaseToSentence = new HashMap<String, String>(10);

    static {
        phaseToSentence.put("m", "la mattina");
        phaseToSentence.put("mp", "la mattina o il pomeriggio");
        phaseToSentence.put("ms", "la mattina o la sera");
        phaseToSentence.put("mps", "tutto il giorno");
        phaseToSentence.put("p", "il pomeriggio");
        phaseToSentence.put("ps", "il pomeriggio o la sera");
        phaseToSentence.put("s", "la sera");
    }

    private static String[] characters = new String[]{"m", "p", "s"};

    public static enum Slot {
        NO_RAIN("Lascia l’omprello a casa, oggi sicuramente non piove!", "Lascia l’omprello a casa, oggi sicuramente non piove!"),
        VERY_LOW("Lascia l’ombrello a casa, oggi non piove!", "Lascia l’ombrello a casa, oggi non piove!"),
        LOW("Non dovrebbe piovere, ma se XXX non vuoi rischiare portalo", "Oggi non dovrebbe piovere, ma se non vuoi rischiare portalo"),
        MEDIUM("Forse piove XXX, per sicurezza portarlo!", "Forse oggi piove, per sicurezza portarlo!"),
        HIGH("E’ molto probabile che piova, in particolar modo XXX. Prendi l’ombrello!", "E’ molto probabile che oggi piova, prendi l’ombrello!"),
        VERY_HIGH("Sicuramente piove, porta l’ombrello, soprattuto se sei fuori XXX!", "Sicuramente oggi piove, porta l’ombrello!");

        private String phrase;
        private String phraseDay;

        private Slot(String phrase, String phraseDay) {
            this.phrase = phrase;
            this.phraseDay = phraseDay;
        }

        private static Slot fromDoubleToSlot(double probability) {
            if (probability < 0.01) {
                return NO_RAIN;
            } else if (probability < 0.03) {
                return VERY_LOW;
            } else if (probability >= 0.03 && probability < 0.2) {
                return LOW;
            } else if (probability >= 0.2 && probability < 0.5) {
                return MEDIUM;
            } else if (probability >= 0.5 && probability < 0.85) {
                return HIGH;
            } else {
                return VERY_HIGH;
            }
        }
    }

    public static Map.Entry<Slot, String> getPhrase(WeatherManager weatherManager, APIManager.Day day) {
        Map<WeatherInfo.Period, WeatherInfo> periodToInfo = weatherManager.getOverview(day);
        double dailyProbability = periodToInfo.get(WeatherInfo.Period.DAILY).getRainProbability();
        // controllo prima la probabilità del giorno
        Slot slot = Slot.fromDoubleToSlot(dailyProbability);
        String stringToRetunr = "";
        switch (slot) {
            case NO_RAIN:
            case VERY_LOW:
                stringToRetunr = slot.phrase;
                break;
            case LOW:
            case MEDIUM:
            case HIGH:
            case VERY_HIGH:
                double morningProbability = periodToInfo.get(WeatherInfo.Period.MORNING).getRainProbability();
                double afternoonProbability = periodToInfo.get(WeatherInfo.Period.AFTERNOON).getRainProbability();
                double eveningProbability = periodToInfo.get(WeatherInfo.Period.EVENING).getRainProbability();
                double[] probabilities = new double[]{morningProbability, afternoonProbability, eveningProbability};
                String rainingPhase = getRainingPhase(probabilities);
                if (rainingPhase.equals("mps")) {
                    stringToRetunr = slot.phraseDay;
                } else {
                    stringToRetunr = slot.phrase.replace("XXX", phaseToSentence.get(rainingPhase));
                }
                break;
        }
        return new AbstractMap.SimpleImmutableEntry<Slot, String>(slot,stringToRetunr);
    }

    private static String getRainingPhase(double[] probabilities) {
        String rainingPhases = "";
        Slot maxPhase = null;
        for (int i = 0; i < 3; i++) {
            Slot s = Slot.fromDoubleToSlot(probabilities[i]);
            if (maxPhase == null || s.ordinal() > maxPhase.ordinal()) {
                maxPhase = s;
                rainingPhases = characters[i];
            } else if (s.ordinal() == maxPhase.ordinal()) {
                rainingPhases += characters[i];
            }
        }
        return rainingPhases;
    }

}
