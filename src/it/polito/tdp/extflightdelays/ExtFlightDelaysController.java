/**
 * Sample Skeleton for 'ExtFlightDelays.fxml' Controller Class
 */

package it.polito.tdp.extflightdelays;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.extflightdelays.model.Airport;
import it.polito.tdp.extflightdelays.model.Model;
import it.polito.tdp.extflightdelays.model.Vicino;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class ExtFlightDelaysController {

	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML // fx:id="compagnieMinimo"
    private TextField compagnieMinimo; // Value injected by FXMLLoader

    @FXML // fx:id="btnAnalizza"
    private Button btnAnalizza; // Value injected by FXMLLoader

    @FXML // fx:id="cmbBoxAeroportoPartenza"
    private ComboBox<Airport> cmbBoxAeroportoPartenza; // Value injected by FXMLLoader

    @FXML // fx:id="btnAeroportiConnessi"
    private Button btnAeroportiConnessi; // Value injected by FXMLLoader

    @FXML // fx:id="cmbBoxAeroportoDestinazione"
    private ComboBox<Airport> cmbBoxAeroportoDestinazione; // Value injected by FXMLLoader

    @FXML // fx:id="numeroTratteTxtInput"
    private TextField numeroTratteTxtInput; // Value injected by FXMLLoader

    @FXML // fx:id="btnCercaItinerario"
    private Button btnCercaItinerario; // Value injected by FXMLLoader

    @FXML
    void doAnalizzaAeroporti(ActionEvent event) {
    	
    	
    	try {
    		
    		Integer compagnie = Integer.parseInt(this.compagnieMinimo.getText());
    		
    		this.model.creaGrafo(compagnie);
    		this.cmbBoxAeroportoPartenza.getItems().clear();
    		this.cmbBoxAeroportoPartenza.getItems().addAll(this.model.vertexSet());
    		this.cmbBoxAeroportoDestinazione.getItems().addAll(this.model.vertexSet());
    		
    	}catch(NumberFormatException e) {
    		this.txtResult.clear();
    		this.txtResult.appendText("Devi inserire un numero minimo di compagnie");
    	}

    }

    @FXML
    void doCalcolaAeroportiConnessi(ActionEvent event) {
    	
    	Airport origine = this.cmbBoxAeroportoPartenza.getValue();
    	
    	if(origine==null) {
    		this.txtResult.clear();
    		this.txtResult.appendText("Attenzione! Seleziona un aeroporto partenza");
    	}else {
    		List<Vicino> vicini = this.model.getVicini(origine);
    		this.txtResult.clear();
    		this.txtResult.appendText("Vicini dell'aeroporto: "+origine+"\n");
    		for(Vicino v: vicini) {
    			this.txtResult.appendText(v.toString()+"\n");
    		}
    	}

    }

    @FXML
    void doCercaItinerario(ActionEvent event) {
    	
    	Airport origine = this.cmbBoxAeroportoPartenza.getValue();
    	Airport destinazione = this.cmbBoxAeroportoDestinazione.getValue();
    	
    	if(origine==null || destinazione==null) {
    		this.txtResult.clear();
    		this.txtResult.appendText("Attenzione selezionare destinazione ed origine");
    	}else {
    		
    		if(origine.equals(destinazione)) {
    			this.txtResult.clear();
    			this.txtResult.appendText("Attenzione selezionare partenza e destinazione diversi");
    		}else {
    			
    			try {
    				Integer tratte = Integer.parseInt(this.numeroTratteTxtInput.getText());
    				
    				List<Airport> aeroportiItinerario = this.model.cercaItinerario(origine,destinazione,tratte);
    				Double peso = this.model.getPeso();
    				
    				this.txtResult.clear();
    				this.txtResult.appendText("Peso: "+peso+"\n");
    				
    				for(Airport a : aeroportiItinerario) {
    					this.txtResult.appendText(a.toString()+"\n");
    				}
    				
    			}catch(NumberFormatException e) {
    				this.txtResult.clear();
    				this.txtResult.appendText("Attenzione! Inserisci un numero di tratte minime");
    			}

    		}
    	}
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'ExtFlightDelays.fxml'.";
        assert compagnieMinimo != null : "fx:id=\"compagnieMinimo\" was not injected: check your FXML file 'ExtFlightDelays.fxml'.";
        assert btnAnalizza != null : "fx:id=\"btnAnalizza\" was not injected: check your FXML file 'ExtFlightDelays.fxml'.";
        assert cmbBoxAeroportoPartenza != null : "fx:id=\"cmbBoxAeroportoPartenza\" was not injected: check your FXML file 'ExtFlightDelays.fxml'.";
        assert btnAeroportiConnessi != null : "fx:id=\"btnAeroportiConnessi\" was not injected: check your FXML file 'ExtFlightDelays.fxml'.";
        assert cmbBoxAeroportoDestinazione != null : "fx:id=\"cmbBoxAeroportoDestinazione\" was not injected: check your FXML file 'ExtFlightDelays.fxml'.";
        assert numeroTratteTxtInput != null : "fx:id=\"numeroTratteTxtInput\" was not injected: check your FXML file 'ExtFlightDelays.fxml'.";
        assert btnCercaItinerario != null : "fx:id=\"btnCercaItinerario\" was not injected: check your FXML file 'ExtFlightDelays.fxml'.";

    }
    
    
    public void setModel(Model model) {
  		this.model = model;
  		
  	}
}
