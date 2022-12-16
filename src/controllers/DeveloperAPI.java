package controllers;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import models.*;
import utils.Utilities;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class DeveloperAPI {

    //TODO Nothing!  The DeveloperAPI class is completed! All you have to do here is implemment the ISerializer interface

    private List<Developer> developers = new ArrayList<>();

    public boolean addDeveloper(Developer developer) {
        if (isValidDeveloper(developer.getDeveloperName())){
            return false;
        }
        return developers.add(developer);
    }

    public Developer getDeveloperByIndex(int index){
        if (Utilities.isValidIndex(developers, index)){
            return developers.get(index);
        }
        else{
            return null;
        }
    }

    public Developer getDeveloperByName (String developerName){
        int index = retrieveDeveloperIndex(developerName);
        if (index != -1){
            return developers.get(index);
        }
        return null;
    }


    public String listDevelopers(){
        String listDevelopers = "";
        for (Developer developer : developers){
            listDevelopers += developers.indexOf(developer) + ": " + developer + "\n";
        }
        if (listDevelopers.equals("")){
            return "No developers";
        }
        else {
            return listDevelopers;
        }
    }

    public boolean updateDeveloperWebsite(String developerName, String developerWebsite){
        if (isValidDeveloper(developerName)){
            Developer developerToUpdate = getDeveloperByName(developerName);
            developerToUpdate.setDeveloperWebsite(developerWebsite);
            return true;
        }
        return false;
    }

    public Developer removeDeveloper(String developerName){
        int index = retrieveDeveloperIndex(developerName);
        if (index != -1) {
            return developers.remove(index);
        }
        return null;
    }

    public boolean isValidDeveloper(String developerName){
        for (Developer developer : developers){
            if (developer.getDeveloperName().equalsIgnoreCase(developerName)){
                return true;
            }
        }
        return false;
    }

    public int retrieveDeveloperIndex(String developerName){
        for (Developer developer : developers){
            if (developer.getDeveloperName().equalsIgnoreCase(developerName)){
                return developers.indexOf(developer);
            }
        }
        return -1;
    }

    public List<Developer> getDevelopers() {
        return developers;
    }

    public void load() throws Exception {

        Class<?>[] classes = new Class[]{Developer.class};

        XStream xstream = new XStream(new DomDriver());
        XStream.setupDefaultSecurity(xstream);
        xstream.allowTypes(classes);
        ObjectInputStream in = xstream.createObjectInputStream(new FileReader(fileName()));
        developers = (List<Developer>) in.readObject();
        in.close();
    }

    public void save() throws Exception {
        XStream xstream = new XStream(new DomDriver());
        ObjectOutputStream out = xstream.createObjectOutputStream(new FileWriter(fileName()));
        out.writeObject(developers);
        out.close();
    }

    public String fileName(){
        return "developers.xml";
    }


}
