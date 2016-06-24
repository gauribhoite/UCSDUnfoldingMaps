package gui;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.GeoJSONReader;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.providers.Google;
import de.fhpotsdam.unfolding.utils.MapUtils;
import processing.core.PApplet;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by gauri_rb on 6/16/16.
 */
public class LifeExpectancy extends PApplet {
    UnfoldingMap map;
    Map <String, Float> lifeExpByCountry;
    List<Feature> countries;
    List<Marker> countryMarkers;

    public void setup(){
        size(800,800,OPENGL);
        map = new UnfoldingMap(this, 50,50,700,500,new Google.GoogleMapProvider());
        MapUtils.createDefaultEventDispatcher(this,map);

        lifeExpByCountry = loadLifeExpectancyFromCSV("data/LifeExpectancyWorldBank.csv");
        countries = GeoJSONReader.loadData(this,"data/countrues.geo.json");
        countryMarkers = MapUtils.createSimpleMarkers(countries);
        map.addMarkers(countryMarkers);
        shadeCountries();
    }

    public void draw(){
        map.draw();
    }

    private void shadeCountries(){
        for(Marker marker:countryMarkers){
            String countryId = marker.getId();

            if(lifeExpByCountry.containsKey(countryId)){
                float lifeExp = lifeExpByCountry.get(countryId);
                int colorLevel = (int) map(lifeExp,40,90,10,255);
                marker.setColor(color(255-colorLevel,100,colorLevel));
            }else{
                marker.setColor(color(150,150,150));
            }
        }
    }

    private Map<String, Float> loadLifeExpectancyFromCSV(String filename){
        Map<String,Float> lifeExpMap = new HashMap<String, Float>();

        String[] rows = loadStrings(filename);

        for(String row:rows){
            String[] columns = row.split(",");
            if(columns[5].length()>0){
                float value = Float.parseFloat(columns[5]);
                lifeExpMap.put(columns[4],value);
            }
        }
        return lifeExpMap;
    }
}
