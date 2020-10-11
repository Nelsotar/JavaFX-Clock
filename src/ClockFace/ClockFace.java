/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClockFace;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 *
 * @author Taryn Nelson
 */
public class ClockFace extends Application{
    double clockRadius;
    double frameWidth;
    double frameHeight;
    double clockCenterX;
    double clockCenterY;
    
    ImageView clockImageView;
    Line hourHand;
    Line minuteHand; 
    Line secondHand;
    Line secondHandTail;
    Circle center;
    Pane clockPane;
    Text currentTime;
    
    Timeline animation;
    EventHandler<ActionEvent> update;
    KeyFrame kf;
    
    public ClockFace(){
        clockRadius = 200;
        frameWidth = 500;
        frameHeight = 550;
        clockCenterX = 250;
        clockCenterY = 250;
    
        clockPane = new Pane();
        currentTime = new Text();
        currentTime.setFont(Font.font(40));
        
        Image clockImage = new Image("images/clockface.jpg");
        clockImageView = new ImageView(clockImage);
        
        update = e->{setTime();};
        kf = new KeyFrame(Duration.ONE, update);
        animation = new Timeline(kf);
    }
    
    public void createClockFace(){
        clockPane.setStyle("-fx-background-color: white;");
        
        clockImageView.setLayoutX(50);
        clockImageView.setLayoutY(50);
           
        currentTime.setLayoutX(150);
        currentTime.setLayoutY(500);
         
        clockPane.getChildren().addAll(clockImageView, currentTime); 
    }
    
    public void setTime(){
        drawHourHand();
        drawMinuteHand();
        drawSecondHand();
        drawCenter();
        setDigitalClock();
    }
    
    public void drawCenter(){
        if(center == null){
            center = new Circle(clockCenterX, clockCenterY, 10);
            center.setStroke(Color.BLACK);
            center.setStrokeWidth(3);
            center.setFill(Color.WHITE);
            clockPane.getChildren().add(center);
        }
    }
    
    public void setDigitalClock(){
        String ampm;
        int hour = getHour();
        int minute = getMinute();
        int second = getSecond();
        String extraZeroMinute = "";
        String extraZeroSecond = "";
        
        if(hour > 12){
            ampm = "PM";
        }else
        {
            ampm = "AM";
        }
        
        if(second < 10){
            extraZeroSecond = "0";
        }
        
        if(minute < 10){
            extraZeroMinute = "0";
        }
        
        hour %= 12;
        
        currentTime.setText(hour + ":" + extraZeroMinute + minute + ":" + extraZeroSecond + second + " " + ampm);
    }
    
     public void drawSecondHand(){
        double radius = clockRadius * 0.8;
        double radiusTail = clockRadius * 0.2;
        
        double angle = (getSecond()/60.0) * 2 * Math.PI; 
        int endX = (int)(clockCenterX + (radius * Math.sin(angle)));
        int endY = (int)(clockCenterY - (radius * Math.cos(angle)));
        
        int endXTail = (int)(clockCenterX - (radiusTail * Math.sin(angle)));
        int endYTail = (int)(clockCenterY + (radiusTail * Math.cos(angle)));
        
        if(secondHand == null){
           secondHand = new Line(clockCenterX, clockCenterY, endX, endY);
           
           secondHand.setStrokeLineCap(StrokeLineCap.ROUND);     
           secondHand.setStrokeWidth(3);
           secondHand.setStroke(Color.RED);
           
           secondHandTail = new Line(clockCenterX, clockCenterY, endX, endY);
           
           secondHandTail.setStrokeLineCap(StrokeLineCap.ROUND);     
           secondHandTail.setStrokeWidth(3);
           secondHandTail.setStroke(Color.RED);
           
           clockPane.getChildren().addAll(secondHand, secondHandTail);     
        }
        else{
            secondHand.setEndX(endX);
            secondHand.setEndY(endY);
            
            secondHandTail.setEndX(endXTail);
            secondHandTail.setEndY(endYTail);
        } 
        
    }
        public void drawMinuteHand(){
       
        double radius = clockRadius * 0.6;
        
        double angle = ((getMinute() + getSecond()/60.0)/60) * 2 * Math.PI; 
        int endX = (int)(clockCenterX + (radius * Math.sin(angle)));
        int endY = (int)(clockCenterY - (radius * Math.cos(angle)));
        
        if(minuteHand == null){
           minuteHand = new Line(clockCenterX, clockCenterY, endX, endY); 
           
           minuteHand.setStrokeLineCap(StrokeLineCap.ROUND);
           minuteHand.setStrokeWidth(16);
           
           clockPane.getChildren().add(minuteHand); 
        }
        else{
            minuteHand.setEndX(endX);
            minuteHand.setEndY(endY);
        } 
        
    }
    
    public void drawHourHand(){
        double hour = getHour() % 12;
        double radius = clockRadius * 0.5;
        
        double angle = ((hour + (getMinute()/60) + (getSecond()/3600))/12) * 2 * Math.PI; 
        int endX = (int)(clockCenterX + (radius * Math.sin(angle)));
        int endY = (int)(clockCenterY - (radius * Math.cos(angle)));
        
        if(hourHand == null){
           hourHand = new Line(clockCenterX, clockCenterY, endX, endY); 
           
           hourHand.setStrokeWidth(20);
           hourHand.setStrokeLineCap(StrokeLineCap.ROUND);
           
           clockPane.getChildren().add(hourHand); 
        }
        else{
            hourHand.setEndX(endX);
            hourHand.setEndY(endY);
        }    
    }
     
    public int getHour(){
       long currentTime = System.currentTimeMillis();
       currentTime /= 1000;
       currentTime %= 86400;
       
       long hour = currentTime / 3600 - 4;
       return (int)hour;
    }
    
    public int getMinute(){
        long currentTime = System.currentTimeMillis();
        currentTime /= 1000;
        currentTime %= 86400;
       
        long minute = (currentTime % 3600) / 60;
        return (int)minute;
    }
    
    public int getSecond(){
        long currentTime = System.currentTimeMillis();
        currentTime /= 1000;
        currentTime %= 86400;
       
        long second = currentTime % 60;
        return (int)second;
    }
    
    @Override
    public void start(Stage stage){   
        
        ClockFace cf = new ClockFace();
        cf.createClockFace();
       
        cf.animation.setDelay(Duration.ZERO);
        cf.animation.setCycleCount(Timeline.INDEFINITE);
        cf.animation.play();
        
        Scene scene = new Scene(cf.clockPane, frameWidth, frameHeight);
        stage.setScene(scene);
        stage.show(); 
        
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
