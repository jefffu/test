package org.jfu.test.javafx;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.effect.Lighting;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.RotateEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.input.SwipeEvent;
import javafx.scene.input.TouchEvent;
import javafx.scene.input.ZoomEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;

public class TestGesture extends Application {

    private int gestureCount;

    @Override
    public void start(Stage primaryStage) throws Exception {
        VBox shapes = new VBox();
        shapes.setAlignment(Pos.CENTER);
        shapes.setPadding(new Insets(15.0));
        shapes.setSpacing(30.0);
        shapes.setPrefWidth(500);
        shapes.getChildren().addAll(createRectangle(), createEllipse());

        primaryStage.setScene(new Scene(shapes, 500, 400));
        primaryStage.setTitle("Hello World!");

        primaryStage.show();

    }

    private Rectangle createRectangle() {
        final Rectangle rect = new Rectangle(100, 100, 100, 100);
        rect.setFill(Color.DARKMAGENTA);

        // Scroll Events
        rect.setOnScroll(new EventHandler<ScrollEvent>() {

            @Override
            public void handle(ScrollEvent event) {
                // TODO Auto-generated method stub
                if (!event.isInertia()) {
                    rect.setTranslateX(rect.getTranslateX() + event.getDeltaX());
                    rect.setTranslateY(rect.getTranslateY() + event.getDeltaY());
                }
                log("Rectangle: Scroll event" + ", inertia: " + event.isInertia() + ", direct: " + event.isDirect());
                event.consume();
            }

        });

        rect.setOnScrollStarted(new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent event) {
                inc(rect);
                log("Rectangle: Scroll started event");
                event.consume();
            }
        });

        rect.setOnScrollFinished(new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent event) {
                dec(rect);
                log("Rectangle: Scroll finished event");
                event.consume();
            }
        });

        // Zoom Events
        rect.setOnZoom(new EventHandler<ZoomEvent>() {
            @Override
            public void handle(ZoomEvent event) {
                rect.setScaleX(rect.getScaleX() * event.getZoomFactor());
                rect.setScaleY(rect.getScaleY() * event.getZoomFactor());
                log("Rectangle: Zoom event" + ", inertia: " + event.isInertia() + ", direct: " + event.isDirect());

                event.consume();
            }
        });

        rect.setOnZoomStarted(new EventHandler<ZoomEvent>() {
            @Override
            public void handle(ZoomEvent event) {
                inc(rect);
                log("Rectangle: Zoom event started");
                event.consume();
            }
        });

        rect.setOnZoomFinished(new EventHandler<ZoomEvent>() {
            @Override
            public void handle(ZoomEvent event) {
                dec(rect);
                log("Rectangle: Zoom event finished");
                event.consume();
            }
        });

        // Rotate Events
        rect.setOnRotate(new EventHandler<RotateEvent>() {
            @Override
            public void handle(RotateEvent event) {
                rect.setRotate(rect.getRotate() + event.getAngle());
                log("Rectangle: Rotate event" + ", inertia: " + event.isInertia() + ", direct: " + event.isDirect());
                event.consume();
            }
        });

        rect.setOnRotationStarted(new EventHandler<RotateEvent>() {
            @Override
            public void handle(RotateEvent event) {
                inc(rect);
                log("Rectangle: Rotate event started");
                event.consume();
            }
        });

        rect.setOnRotationFinished(new EventHandler<RotateEvent>() {
            @Override
            public void handle(RotateEvent event) {
                dec(rect);
                log("Rectangle: Rotate event finished");
                event.consume();
            }
        });

        // Swipe Events
        rect.addEventHandler(SwipeEvent.ANY, new EventHandler<SwipeEvent>() {

            @Override
            public void handle(SwipeEvent event) {
                log("Rectangle: Swipe event " + event.getEventType() + ", touch count: " + event.getTouchCount());
                event.consume();
            }
            
        });
//        rect.setOnSwipeRight(new EventHandler<SwipeEvent>() {
//            @Override
//            public void handle(SwipeEvent event) {
//                log("Rectangle: Swipe right event, touch count: " + event.getTouchCount());
//                event.consume();
//            }
//        });
//
//        rect.setOnSwipeLeft(new EventHandler<SwipeEvent>() {
//            @Override
//            public void handle(SwipeEvent event) {
//                log("Rectangle: Swipe left event, touch count: " + event.getTouchCount());
//                event.consume();
//            }
//        });
//
//        rect.setOnSwipeUp(new EventHandler<SwipeEvent>() {
//            @Override
//            public void handle(SwipeEvent event) {
//                log("Rectangle: Swipe up event, touch count: " + event.getTouchCount());
//                event.consume();
//            }
//        });
//
//        rect.setOnSwipeDown(new EventHandler<SwipeEvent>() {
//            @Override
//            public void handle(SwipeEvent event) {
//                log("Rectangle: Swipe down event, touch count: " + event.getTouchCount());
//                event.consume();
//            }
//        });

        // Touch Events
        rect.setOnTouchPressed(new EventHandler<TouchEvent>() {
            @Override
            public void handle(TouchEvent event) {
                log("Rectangle: Touch pressed event, touch count: " + event.getTouchPoints().size());
                event.consume();
            }
        });

        rect.setOnTouchReleased(new EventHandler<TouchEvent>() {
            @Override
            public void handle(TouchEvent event) {
                log("Rectangle: Touch released event, touch count: " + event.getTouchCount());
                event.consume();
            }
        });

        return rect;
    }

    private Ellipse createEllipse() {
        final Ellipse oval = new Ellipse(100, 50);
        oval.setFill(Color.STEELBLUE);

        // Mouse Events
        oval.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.isSynthesized()) {
                    log("Ellipse: Mouse pressed event from touch" + ", synthesized: " + event.isSynthesized());
                } else {
                    log("Ellipse: Mouse pressed event" + ", synthesized: " + event.isSynthesized());
                }
                event.consume();
            }
        });

        oval.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.isSynthesized()) {
                    log("Ellipse: Mouse released event from touch" + ", synthesized: " + event.isSynthesized());
                } else {
                    log("Ellipse: Mouse released event" + ", synthesized: " + event.isSynthesized());
                }
                event.consume();
            }
        });

        return oval;
    }

    private void log(String log) {
        System.out.println(log);
    }

    private void inc(Shape shape) {
        if (gestureCount == 0) {
            shape.setEffect(new Lighting());
        }
        gestureCount++;
    }

    private void dec(Shape shape) {
        gestureCount--;
        if (gestureCount == 0) {
            shape.setEffect(null);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
