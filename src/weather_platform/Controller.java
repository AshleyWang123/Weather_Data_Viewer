package weather_platform;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.cell.PropertyValueFactory;
import java.io.*;
import java.text.DecimalFormat;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.util.*;
import java.util.List;

public class Controller {

    // defining the variables
    final File folder = new File("src/weather_platform/dataset");
    HashMap<String,String> map = new HashMap<String,String>();
    ObservableList<data2019> dataList = FXCollections.observableArrayList();
    FilteredList<data2019> filteredData = new FilteredList<>(dataList, p -> true);
    SortedList<data2019> sortedData = new SortedList<>(filteredData);
    List<String> filenames = new ArrayList<>();
    List<Float> tmax = new ArrayList<>();
    List<Float> tmin = new ArrayList<>();
    List<Integer> ta  = new ArrayList<>();
    List<Float> tr = new ArrayList<>();
    List<String> Name9 = new ArrayList<>();
    List<Float> Tmax9 = new ArrayList<>();
    List<Float> Tmin9 = new ArrayList<>();
    List<Integer> Ta9 = new ArrayList<>();
    List<Float> Tr9 = new LinkedList<>();
    List<Float> High = new ArrayList<>();
    List<Float> Low = new ArrayList<>();
    List<Float> Aaf = new ArrayList<>();
    List<Float> Aar = new LinkedList<>();
    List<Float> RHigh = new ArrayList<>();
    List<Float> RLow = new ArrayList<>();
     List<String> RHym = new ArrayList<>();
    List<String> RLym = new ArrayList<>();
    List<Float> RAaf = new ArrayList<>();
    List<Float> RAar = new LinkedList<>();
    List<String> Year = new LinkedList<>();
    List<String> Month = new LinkedList<>();

    @FXML TextField filterField;
    @FXML TableView tableView;
    @FXML TabPane tabPane;
    @FXML TableColumn tc1;
    @FXML TableColumn tc2;
    @FXML TableColumn tc3;
    @FXML TableColumn tc4;
    @FXML TableColumn tc5;
    @FXML Button btnlog;
    @FXML Button btn;
    @FXML Tab Tab1;

    public void listFilesForFolder(final File folder) {
        for (final File file : folder.listFiles()) {

            //loop over the filenames list
            if (file.isDirectory()) {
                listFilesForFolder(file);//in case for subfolder
            } else {
                if(file.getName().contains(".csv"))
                    map.put("name",file.getName().replaceAll("\\..*",""));
                    filenames.add(file.getName().replaceAll("\\..*",""));
            }

            //load Details from files.
            try{
                String data;
                Scanner sc= new Scanner(file);
                for (int i=0; i<108; i++){
                if (sc.hasNext()) {
                    data = sc.next();
                }else {
                    data="0000,0,0.0,0.0,0,0.0";//when the csv is empty or incomplete
                }
                    String[] values = data.split(",");
                    map.put("year", values[0]);
                    map.put("month", values[1]);
                    map.put("tmax", values[2]);
                    map.put("tmin", values[3]);
                    map.put("af", values[4]);
                    map.put("rain", values[5]);

                    lastyeardata();
                    dataReport();
                }
                sc.close();
            } catch (IOException e) {
                e.printStackTrace();}
        }
    }

    public class data2019{

        //the format of the Details
        private SimpleStringProperty Name;
        private SimpleFloatProperty Tmax,Tmin,Rain;
        private SimpleIntegerProperty Af;

        public String getName(){
            return  Name.get();
        }

        public float getTmax(){
            return  Tmax.get();
        }

        public float getTmin(){
            return  Tmin.get();
        }

        public float getRain(){
            return  Rain.get();
        }

        public int getAf(){
            return  Af.get();
        }

        data2019(String Name, float Tmax, float Tmin, int Af, float Rain ){
            this.Name=new SimpleStringProperty(Name);
            this.Tmax=new SimpleFloatProperty(Tmax);
            this.Tmin=new SimpleFloatProperty(Tmin);
            this.Af=new SimpleIntegerProperty(Af);
            this.Rain=new SimpleFloatProperty(Rain);
        }
    }

    private void lastyeardata (){

        //get 2019 Details
        if ((map.get("year").contains("2019"))){
            tmax.add(Float.valueOf(map.get("tmax")));
            tmin.add(Float.valueOf(map.get("tmin")));
            ta.add(Integer.valueOf(map.get("af")));
            tr.add(Float.valueOf(map.get("rain")));
        }
        if (tmax.size()>=12){
            Tmax9.add(Collections.max(tmax));
            Tmin9.add(Collections.min(tmin));
            Name9.add(map.get("name"));

            int suma=0; float sumr=0;
            for (int i:ta){
                suma=suma+i;
            }
            Ta9.add(suma);

            for (float k:tr){
                sumr=sumr+k;
            }
            DecimalFormat df= new DecimalFormat("#.##");
            Tr9.add(Float.valueOf(df.format(sumr)));

            tmax.clear();
            tmin.clear();
            ta.clear();
            tr.clear();
        }

    }

    private void present2019(){
        Tab1.setClosable(false);

        for(int i=0; i<Name9.size();i++){
               dataList.add(new data2019(
                       Name9.get(i),Tmax9.get(i),Tmin9.get(i),Ta9.get(i),Tr9.get(i)));
          }

        for (String name: filenames) {
            if (!Name9.contains(name)){
                dataList.add(new data2019(name,0,0,0,0));
            }
        }

        tc1.setCellValueFactory(new PropertyValueFactory<>("Name"));
        tc2.setCellValueFactory(new PropertyValueFactory<>("Tmax"));
        tc3.setCellValueFactory(new PropertyValueFactory<>("Tmin"));
        tc4.setCellValueFactory(new PropertyValueFactory<>("Af"));
        tc5.setCellValueFactory(new PropertyValueFactory<>("Rain"));

        //search function
        filterField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(data2019 -> {

                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if (String.valueOf(data2019.getName()).toLowerCase().contains(lowerCaseFilter)) {
                    return true;

                } else if (String.valueOf(data2019.getTmax()).toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                else if (String.valueOf(data2019.getTmin()).toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                else if (String.valueOf(data2019.getRain()).toLowerCase().contains(lowerCaseFilter)) {
                    return true;

                }else if (String.valueOf(data2019.getAf()).toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }

                return false; // Does not match.
            });
        });
        sortedData.comparatorProperty().bind(tableView.comparatorProperty());

        //Add sorted (and filtered) Details to the table.
        tableView.setItems(sortedData);

    }

    public void dataReport(){

        //for the report of all Details
        High.add(Float.valueOf(map.get("tmax")));
        Low.add(Float.valueOf(map.get("tmin")));
        Aaf.add(Float.valueOf(map.get("af")));
        Aar.add(Float.valueOf(map.get("rain")));
        Year.add(map.get("year"));
        Month.add(map.get("month"));
       if (High.size()>=108){

        float suma=0; float sumr=0;
        for (float i:Aaf){
            suma=suma+i;
        }
        for (float k:Aar){
            sumr=sumr+k;
        }
        DecimalFormat df= new DecimalFormat("#.##");
        RHigh.add(Collections.max(High));
        RHym.add(Month.get(High.indexOf(Collections.max(High)))+"/"
                +Year.get(High.indexOf(Collections.max(High))));
        RLow.add(Collections.min(Low));
        RLym.add(Month.get(Low.indexOf(Collections.min(Low)))+"/"
                +Year.get(Low.indexOf(Collections.min(Low))));

        RAaf.add(Float.valueOf(df.format(suma/Aaf.size()*12)));
        RAar.add(Float.valueOf(df.format(sumr/Aar.size()*12)));

        High.clear();
        Low.clear();
        Aaf.clear();
        Aar.clear();
        Year.clear();
        Month.clear();
        }

    }

    public void presentReport(){

        //write Details in the txt and open(pop up)
       try {
           PrintWriter out = new PrintWriter("src/weather_platform/Report.txt");
           out.println("**When the year is\"0000\", it means the station lack of data.**"+"\n\n");
           for (int i=0;i<RHigh.size();i++) {
               out.println("Number: "+(i+1));
               out.println("Station: "+filenames.get(i));
               out.println("Highest: "+RHym.get(i)+" : "+RHigh.get(i));
               out.println("Lowest: "+RLym.get(i)+" : "+RLow.get(i));
               out.println("Average annual af: "+RAaf.get(i));
               out.println("Average annual rainfall: "+RAar.get(i)+"\n");
           }

           out.close();
       }catch (IOException e){
            e.printStackTrace();
       }

        //clicks and opens the Report.txt
        btn.setOnAction(actionEvent ->  {
            Runtime runtime = Runtime.getRuntime();
            try {
                runtime.exec("xdg-open src/weather_platform/Report.txt");
            }catch (IOException e){
                System.err.println(e);
            }
        });

    }

    public void clickNstore(){

        //click function and store the name in Response
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.SELECTED_TAB);
        tableView.setRowFactory( tv -> {
            TableRow<data2019> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    data2019 rowData = row.getItem();
                    Tab tab = new Tab(rowData.Name.getValue());
                    try {

                        //store the name in the Response while users click
                        FileOutputStream Response = new FileOutputStream(new File("src/weather_platform/Response"));
                        Response.write(rowData.Name.getValue().getBytes());

                        //loading the corresponding Details
                        tabPane.getTabs().add((tab));
                        tab.setContent((Node)FXMLLoader.load(this.getClass().getResource("resources/Details.fxml")));
                        this.getClass().getResource("style.css").toExternalForm();

                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
            });
            return row ;
        });
    }

    @FXML
    private void logout(ActionEvent event){

        //leave the current page and return Login page
        try {
        Stage stage1=(Stage)btnlog.getScene().getWindow();
        stage1.close();
        Stage stage2=new Stage();
        Parent root=FXMLLoader.load(this.getClass().getResource("resources/Login.fxml"));
        Scene scene= new Scene(root,900,1000);
        stage2.setScene(scene);
        scene.getStylesheets().add(Controller.class.getResource("style.css").toExternalForm());
        stage2.show();

        }catch (Exception e){
        e.printStackTrace();
        }
    }

    public void initialize(){

        //loading csv files
        listFilesForFolder(folder);

        //the last year Details shows
        present2019();

        //clicking and storing the Details then passing to another controller-Details.java
        clickNstore();

        //the report shows
        presentReport();

    }
}


