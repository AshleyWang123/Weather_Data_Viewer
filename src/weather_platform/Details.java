package weather_platform;

import java.io.*;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class Details {

    // defining the variables
    final File folder = new File("src/weather_platform/dataset");
    ObservableList<Alldata> dataList = FXCollections.observableArrayList();
    FilteredList<Alldata> filteredData = new FilteredList<>(dataList, p -> true);
    SortedList<Alldata> sortedData = new SortedList<>(filteredData);
    String name;

    @FXML StackedBarChart stackedBarChart;
    @FXML BarChart barChart;
    @FXML TableView tableView;
    @FXML TableColumn tc1;
    @FXML TableColumn tc2;
    @FXML TableColumn tc3;
    @FXML TableColumn tc4;
    @FXML TableColumn tc5;
    @FXML TableColumn tc6;

    public class Alldata{

        //the format of the Details
        private SimpleStringProperty Year,Month;
        private SimpleFloatProperty Tmax,Tmin,Af, Rain;

        public String getYear(){
            return  Year.get();
        }

        public String getMonth(){
            return  Month.get();
        }

        public Float getTmax(){
            return  Tmax.get();
        }

        public Float getTmin(){
            return  Tmin.get();
        }

        public Float getRain(){
            return  Rain.get();
        }

        public Float getAf(){
            return  Af.get();
        }

        Alldata(String Year, String Month, Float Tmax, Float Tmin, Float Af, Float Rain ){
            this.Year=new SimpleStringProperty(Year);
            this.Month=new SimpleStringProperty(Month);
            this.Tmax=new SimpleFloatProperty(Tmax);
            this.Tmin=new SimpleFloatProperty(Tmin);
            this.Af=new SimpleFloatProperty(Af);
            this.Rain=new SimpleFloatProperty(Rain);
        }
    }

    public void presentAll() {

        //showing the Details
        tc1.setCellValueFactory(new PropertyValueFactory<>("Year"));
        tc2.setCellValueFactory(new PropertyValueFactory<>("Month"));
        tc3.setCellValueFactory(new PropertyValueFactory<>("Tmax"));
        tc4.setCellValueFactory(new PropertyValueFactory<>("Tmin"));
        tc5.setCellValueFactory(new PropertyValueFactory<>("Af"));
        tc6.setCellValueFactory(new PropertyValueFactory<>("Rain"));

        tableView.setPlaceholder(new Label("There is no Details for "+name+" Station"));
        tableView.setItems(dataList);

    }

    public void readCSVNchart() {

        //reads CSVs and shows chart
        barChart.getXAxis().setLabel("Year");
        barChart.setTitle("2011--2013/2019");
        XYChart.Series tmaxM = new XYChart.Series();
        XYChart.Series tminM = new XYChart.Series();
        XYChart.Series afM = new XYChart.Series();
        XYChart.Series trM = new XYChart.Series();
        tmaxM.setName("tmax");
        tminM.setName("tmin");
        afM.setName("af");
        trM.setName("rain");

        stackedBarChart.getXAxis().setLabel("Month");
        stackedBarChart.setTitle("January--Decembe");
        XYChart.Series tmaxY = new XYChart.Series();
        XYChart.Series tminY = new XYChart.Series();
        XYChart.Series afY = new XYChart.Series();
        XYChart.Series trY = new XYChart.Series();
        tmaxY.setName("tmax");
        tminY.setName("tmin");
        afY.setName("af");
        trY.setName("rain");

        for (final File file : folder.listFiles()) {
         if (file.getName().replaceAll("\\..*","").equals(name)){
                try {
                  BufferedReader  br = new BufferedReader(new FileReader(file));

                    String line;
                    while ((line = br.readLine()) != null) {
                        String[] fields = line.split(",");
                        Alldata record = new Alldata(fields[0], fields[1], Float.valueOf(fields[2]),
                                Float.valueOf(fields[3]), Float.valueOf(fields[4]), Float.valueOf(fields[5]));
                        dataList.add(record);

                        tmaxM.getData().add(new XYChart.Data(fields[1], record.getTmax()));
                        tminM.getData().add(new XYChart.Data(fields[1], record.getTmin()));
                        afM.getData().add(new XYChart.Data(fields[1], record.getAf()));
                        trM.getData().add(new XYChart.Data(fields[1], record.getRain()));
                    }
                } catch (IOException ex) {
                    Logger.getLogger(Details.class.getName())
                            .log(Level.SEVERE, null, ex);
                }

                //sum the values by year
                Map<String,Double> max0= dataList.stream().collect(Collectors.groupingBy(
                     Alldata::getYear,Collectors.summingDouble(Alldata::getTmax)));
                 Map<String,Double> min= dataList.stream().collect(Collectors.groupingBy(
                         Alldata::getYear,Collectors.summingDouble(Alldata::getTmin)));
                 Map<String,Double> af= dataList.stream().collect(Collectors.groupingBy(
                         Alldata::getYear,Collectors.summingDouble(Alldata::getAf)));
                 Map<String,Double> rain= dataList.stream().collect(Collectors.groupingBy(
                         Alldata::getYear,Collectors.summingDouble(Alldata::getRain)));

                 // sort value for Descending order
                 Map<String, Double>max= new TreeMap<>(max0);

                 max.forEach((k,v)->{tmaxY.getData().add(new XYChart.Data(k,v));});
                 min.forEach((k,v)->{tminY.getData().add(new XYChart.Data(k,v));});
                 af.forEach((k,v)->{afY.getData().add(new XYChart.Data(k,v));});
                 rain.forEach((k,v)->{trY.getData().add(new XYChart.Data(k,v));});

                 stackedBarChart.getData().addAll(tmaxM, tminM, afM, trM);
                 barChart.getData().addAll(tmaxY,tminY,afY,trY);
         }
        }

    }

    public void readName() {

        //read name from Response
        File file = new File("src/weather_platform/Response");
        try{
            Scanner inputScanner = new Scanner(file);
            name=inputScanner.nextLine();
            readCSVNchart();

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void initialize(){

        //loading function to get the corresponding name
        readName();

        //showing the Details
        presentAll();
    }
}



