/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Utils;

/**
 *
 * @author gauravdahal
 */
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;

public class DialogUtils {

    public static void showSuccessDialog(String successMessage) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(successMessage);
        alert.getButtonTypes().clear();
        
        ButtonType okButton = new ButtonType("OK", ButtonData.OK_DONE);
        alert.getButtonTypes().add(okButton);
        
        alert.showAndWait();
    }

    public static void showErrorDialog(String errorMessage) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(errorMessage);
        alert.showAndWait();
    }
    
    public static void showWarningDialog(String errorMessage) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(null);
        alert.setContentText(errorMessage);
        alert.showAndWait();
    }
    
    public static void showGenerateReport(String message,String onSuccessMessage){
        Alert progressDialog = new Alert(AlertType.INFORMATION);
        progressDialog.setTitle("Report");
        progressDialog.setHeaderText(message);
           
        ProgressBar progressBar = new ProgressBar();
        progressDialog.getDialogPane().setContent(progressBar);

        // Create a task to simulate report generation
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                for (int i = 0; i < 100; i++) {
                    updateProgress(i, 100); // Update progress
                    Thread.sleep(50); // Simulate some work
                }

                return null;
            }
        };

        // Bind the progress bar's progress to the task's progress
        progressBar.progressProperty().bind(task.progressProperty());

        // Show the progress dialog
        progressDialog.show();

        // Execute the task
        task.setOnSucceeded((WorkerStateEvent event) -> {
            // Hide the progress dialog when the task is finished
            progressDialog.setResult(ButtonType.OK);
            progressDialog.close();
            showSuccessDialog(onSuccessMessage);
        });

        // Start the task
        new Thread(task).start();
    }
    
    
    public static void showProgressDialog(String message, Task<?> task) {
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setTitle(message);
        alert.setHeaderText(null);
        alert.setContentText(message);

        ProgressBar progressBar = new ProgressBar();
        progressBar.progressProperty().bind(task.progressProperty());

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setContent(new VBox(progressBar));

        alert.setOnCloseRequest(event -> {
            task.cancel(); // Cancel the task if the dialog is closed
        });

        task.setOnCancelled(event -> {
            alert.close(); // Close the dialog if the task is cancelled
        });

        Thread thread = new Thread(task);
        thread.setDaemon(true); // Set the thread as daemon to prevent blocking the application exit
        thread.start();

        alert.show();
    }

}

