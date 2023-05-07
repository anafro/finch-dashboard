module ru.anafro.finch.finchrobotproject {
    requires javafx.controls;
    requires javafx.fxml;
        requires javafx.web;
            
        requires org.controlsfx.controls;
                requires net.synedra.validatorfx;
            requires org.kordamp.ikonli.javafx;
        
    opens ru.anafro.finch.finchrobotproject to javafx.fxml;
    exports ru.anafro.finch.finchrobotproject;
}