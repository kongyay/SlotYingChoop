package slotyingchoop;

import java.io.File;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;

public class SlotYingChoop extends Application {

    private StackPane pane;
    private Scene scene;
    private Player playerA;
    private Player playerB;
    private int score = 0;
    private Slotmachine slotA;
    private Slotmachine slotB;
    MediaPlayer bgmPlayer;

    private int timeUse = 3;
    private int drawCount = 0;

    // Dmg Rectangle
    private Rectangle rectA = new Rectangle(300, 50);
    private Rectangle rectB = new Rectangle(300, 50);
    // BGs UI
    private ImageView imgView_UI = new ImageView(new Image("image/MainUI.png", 600, 800, true, true));
    private ImageView imgView_startUI = new ImageView(new Image("image/MainMenu.png", 600, 800, true, true));
    // BGs
    private ImageView imgView_RedBG = new ImageView(new Image("image/RedSlot.png", 600, 800, true, true));
    private ImageView imgView_BlueBG = new ImageView(new Image("image/BlueSlot.png", 600, 800, true, true));
    private ImageView imgView_swapUI = new ImageView(new Image("image/SwapPhase.png", 600, 800, true, true));
    // Slots Graphic
    private ImageView[] imgView_slotA = new ImageView[3];
    private ImageView[] imgView_slotB = new ImageView[3];
    // SwapTime Graphic
    private ImageView imgView_swapTimeA = new ImageView(new Image("image/BlueSwapTime.png", 600, 800, true, true));
    private ImageView imgView_swapTimeB = new ImageView(new Image("image/RedSwapTime.png", 600, 800, true, true));
    // Spin Graphics
    private ImageView imgView_spinA = new ImageView(new Image("image/BlueSpin.png", 600, 800, true, true));
    private ImageView imgView_spinB = new ImageView(new Image("image/RedSpin.png", 600, 800, true, true));
    // BGM
    private Media battleBGMSound;
    private Media slotSound = new Media(new File("src/sound/SE_Slot.mp3").toURI().toString());
    private Media voiceStart = new Media(new File("src/sound/VE_Start.mp3").toURI().toString());
    private Media finalSound = new Media(new File("src/sound/SE_FinalResults.mp3").toURI().toString());
    private Media clickSound = new Media(new File("src/sound/SE_Click.mp3").toURI().toString());
    private Media secSound = new Media(new File("src/sound/SE_10sec.mp3").toURI().toString());
    private Media swapSound = new Media(new File("src/sound/SE_Swap.mp3").toURI().toString());
    private Media drawSound = new Media(new File("src/sound/SE_Draw.mp3").toURI().toString());
    private Media mainBGMSound = new Media(new File("src/sound/BGM_Main.mp3").toURI().toString());
    // Counter
    private Label counter = new Label("");
    // Ready
    private ImageView imgView_readyA = new ImageView(new Image("image/ReadyA.png", 600, 800, true, true));
    private ImageView imgView_readyB = new ImageView(new Image("image/ReadyB.png", 600, 800, true, true));

    @Override
    public void start(Stage primaryStage) {
        pane = new StackPane();
        scene = new Scene(pane, 600, 800);

        playerA = new Player();
        playerB = new Player();

        slotA = new Slotmachine(playerA);
        slotB = new Slotmachine(playerB);

        counter.setFont(Font.font("Arial", FontWeight.BOLD, 50));

        primaryStage.setTitle("Slot-Ying-Choop");
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();

        startMenu();

    }

    private void startMenu() {

        pane.getChildren().add(imgView_startUI);
        pane.getChildren().add(imgView_UI);
        score = 0;
        drawCount = 0;

        bgmPlayer = new MediaPlayer(mainBGMSound);
        bgmPlayer.play();

        boolean[] ready = new boolean[2];
        scene.setOnKeyPressed((KeyEvent event) -> {
            if (event.getCode() == KeyCode.S) {
                if (!ready[0]) {
                    pane.getChildren().add(imgView_readyA);
                    ready[0] = true;
                } else {
                    pane.getChildren().remove(imgView_readyA);
                    ready[0] = false;
                }
            }
            if (event.getCode() == KeyCode.K || event.getCode() == KeyCode.NUMPAD5) {
                if (!ready[1]) {
                    pane.getChildren().add(imgView_readyB);
                    ready[1] = true;
                } else {
                    pane.getChildren().remove(imgView_readyB);
                    ready[1] = false;
                }

            }
            if (ready[0] && ready[1]) {
                pane.getChildren().remove(imgView_startUI);
                pane.getChildren().remove(imgView_readyA);
                pane.getChildren().remove(imgView_readyB);
                scene.setOnKeyPressed(null);
                bgmPlayer.stop();
                startSlot(45);
            }
        });

    }

    private void startSlot(int time) {
        System.out.println("Start Slot");

        battleBGMSound = new Media(new File("src/sound/BGM_Battle_" + ((int) (Math.random() * 5) + 1) + ".mp3").toURI().toString());
        bgmPlayer = new MediaPlayer(battleBGMSound);

        if (!pane.getChildren().contains(imgView_swapUI)) {
            pane.getChildren().add(imgView_swapUI);
        }
        if (!pane.getChildren().contains(imgView_RedBG)) {
            pane.getChildren().add(imgView_RedBG);
        }
        if (!pane.getChildren().contains(imgView_BlueBG)) {
            pane.getChildren().add(imgView_BlueBG);
        }

        // A,B animation in
        TranslateTransition ttA = new TranslateTransition(Duration.millis(200), imgView_BlueBG);
        ttA.setFromX(-300);
        ttA.setToX(0);
        ttA.play();
        TranslateTransition ttB = new TranslateTransition(Duration.millis(200), imgView_RedBG);
        ttB.setFromX(300);
        ttB.setToX(0);
        ttB.play();

        imgView_RedBG.toBack();
        imgView_BlueBG.toBack();
        imgView_swapUI.toBack();

        counter.setText(String.valueOf(time));
        counter.setTranslateY(-350);
        if (!pane.getChildren().contains(counter)) {
            pane.getChildren().add(counter);
        }

        updateScoreBar(0);
        rectA.setFill(Paint.valueOf("blue"));
        rectB.setFill(Paint.valueOf("red"));
        if (!pane.getChildren().contains(rectA)) {
            pane.getChildren().add(rectA);
        }
        if (!pane.getChildren().contains(rectB)) {
            pane.getChildren().add(rectB);
        }

        boolean[] isASpin = new boolean[4], isBSpin = new boolean[4];

        for (int i = 0; i < 3; i++) {
            imgView_slotA[i] = new ImageView(slotA.getRuneShow(i).getImage());
            pane.getChildren().add(imgView_slotA[i]);
            imgView_slotA[i].setTranslateX((93 + 1.0) * i + 56 - 300);
            imgView_slotA[i].setTranslateY(-99);
            imgView_slotB[i] = new ImageView(slotB.getRuneShow(i).getImage());
            pane.getChildren().add(imgView_slotB[i]);
            imgView_slotB[i].setTranslateX((93 + 1.0) * i + 56);
            imgView_slotB[i].setTranslateY(-99);
            isASpin[i] = true;
            isBSpin[i] = true;
        }

        int[] selectedA = new int[2], selectedB = new int[2];
        for (int i = 0; i < 2; i++) {
            selectedA[i] = -1;
            selectedB[i] = -1;
        }

        EventHandler<KeyEvent> keyHandler = (KeyEvent event) -> {
            int i;
            if (playerA.getItemLength() < 9) {
                if (event.getCode() == KeyCode.S) {
                    if (isASpin[0] || isASpin[1] || isASpin[2]) {
                        System.out.println("SLOT A STOP !");
                        for (i = 0; !isASpin[i]; i++);
                        isASpin[i] = false;
                    } else {

                        MediaPlayer slotSoundPlayer = new MediaPlayer(slotSound);
                        slotSoundPlayer.play();
                        for (i = 0; i < 3; i++) {
                            isASpin[i] = true;
                        }
                        isASpin[3] = false;
                        if (pane.getChildren().contains(imgView_spinA)) {
                            pane.getChildren().remove(imgView_spinA);
                        }
                    }
                }
            }
            if (playerB.getItemLength() < 9) {
                if (event.getCode() == KeyCode.K || event.getCode() == KeyCode.NUMPAD5) {
                    if (isBSpin[0] || isBSpin[1] || isBSpin[2]) {
                        System.out.println("SLOT B STOP !");
                        for (i = 0; !isBSpin[i]; i++);
                        isBSpin[i] = false;
                    } else {

                        MediaPlayer slotSoundPlayer = new MediaPlayer(slotSound);
                        slotSoundPlayer.play();
                        for (i = 0; i < 3; i++) {
                            isBSpin[i] = true;
                        }
                        isBSpin[3] = false;
                        if (pane.getChildren().contains(imgView_spinB)) {
                            pane.getChildren().remove(imgView_spinB);
                        }
                    }
                }
            }

            MediaPlayer clickPlayerA = new MediaPlayer(clickSound);
            if (playerA.getItemLength() == 9) {
                if (null != event.getCode()) {

                    switch (event.getCode()) {
                        case Q:
                            if (selectedA[0] == -1) {
                                selectedA[0] = 0;
                            } else {
                                selectedA[1] = 0;
                            }
                            break;
                        case W:
                            if (selectedA[0] == -1) {
                                selectedA[0] = 1;
                            } else {
                                selectedA[1] = 1;
                            }
                            break;
                        case E:
                            if (selectedA[0] == -1) {
                                selectedA[0] = 2;
                            } else {
                                selectedA[1] = 2;
                            }
                            break;
                        case A:
                            if (selectedA[0] == -1) {
                                selectedA[0] = 3;
                            } else {
                                selectedA[1] = 3;
                            }
                            break;
                        case S:
                            if (selectedA[0] == -1) {
                                selectedA[0] = 4;
                            } else {
                                selectedA[1] = 4;
                            }
                            break;
                        case D:
                            if (selectedA[0] == -1) {
                                selectedA[0] = 5;
                            } else {
                                selectedA[1] = 5;
                            }
                            break;
                        case Z:
                            if (selectedA[0] == -1) {
                                selectedA[0] = 6;
                            } else {
                                selectedA[1] = 6;
                            }
                            break;
                        case X:
                            if (selectedA[0] == -1) {
                                selectedA[0] = 7;
                            } else {
                                selectedA[1] = 7;
                            }
                            break;
                        case C:
                            if (selectedA[0] == -1) {
                                selectedA[0] = 8;
                            } else {
                                selectedA[1] = 8;
                            }
                            break;
                        default:
                            break;
                    }

                }
                if (selectedA[0] != -1) {
                    InnerShadow is1 = new InnerShadow();
                    is1.setColor(Color.web("#0044f0"));
                    is1.setRadius(5);
                    is1.setChoke(0.4);
                    playerA.getItemImageView(selectedA[0]).setEffect(is1);
                    clickPlayerA.play();
                }
                if (selectedA[0] != -1 && selectedA[1] != -1) {

                    playerA.shuffleItem(selectedA[0], selectedA[1]);

                    TranslateTransition tt0 = new TranslateTransition(Duration.millis(500), playerA.getItemImageView(selectedA[0]));
                    tt0.setToX(getXPosItem(playerA, selectedA[0]));
                    tt0.setToY(getYPosItem(playerA, selectedA[0]));
                    tt0.play();

                    TranslateTransition tt1 = new TranslateTransition(Duration.millis(500), playerA.getItemImageView(selectedA[1]));
                    tt1.setToX(getXPosItem(playerA, selectedA[1]));
                    tt1.setToY(getYPosItem(playerA, selectedA[1]));
                    tt1.play();

                    for (i = 0; i < 2; i++) {
                        playerA.getItemImageView(selectedA[i]).setEffect(null);
                        selectedA[i] = -1;
                    }
                    clickPlayerA.stop();
                    clickPlayerA = new MediaPlayer(swapSound);
                    clickPlayerA.play();
                    updateScoreBar(calculateFight());
                }
            }

            MediaPlayer clickPlayerB = new MediaPlayer(clickSound);
            if (playerB.getItemLength() == 9) {
                if (null != event.getCode()) {
                    switch (event.getCode()) {
                        case I:
                        case NUMPAD7:
                            if (selectedB[0] == -1) {
                                selectedB[0] = 0;
                            } else {
                                selectedB[1] = 0;
                            }
                            break;
                        case O:
                        case NUMPAD8:
                            if (selectedB[0] == -1) {
                                selectedB[0] = 1;
                            } else {
                                selectedB[1] = 1;
                            }
                            break;
                        case P:
                        case NUMPAD9:
                            if (selectedB[0] == -1) {
                                selectedB[0] = 2;
                            } else {
                                selectedB[1] = 2;
                            }
                            break;
                        case J:
                        case NUMPAD4:
                            if (selectedB[0] == -1) {
                                selectedB[0] = 3;
                            } else {
                                selectedB[1] = 3;
                            }
                            break;
                        case K:
                        case NUMPAD5:
                            if (selectedB[0] == -1) {
                                selectedB[0] = 4;
                            } else {
                                selectedB[1] = 4;
                            }
                            break;
                        case L:
                        case NUMPAD6:
                            if (selectedB[0] == -1) {
                                selectedB[0] = 5;
                            } else {
                                selectedB[1] = 5;
                            }
                            break;
                        case N:
                        case NUMPAD1:
                            if (selectedB[0] == -1) {
                                selectedB[0] = 6;
                            } else {
                                selectedB[1] = 6;
                            }
                            break;
                        case M:
                        case NUMPAD2:
                            if (selectedB[0] == -1) {
                                selectedB[0] = 7;
                            } else {
                                selectedB[1] = 7;
                            }
                            break;
                        case COMMA:
                        case NUMPAD3:
                            if (selectedB[0] == -1) {
                                selectedB[0] = 8;
                            } else {
                                selectedB[1] = 8;
                            }
                            break;
                        default:
                            break;
                    }

                }
                if (selectedB[0] != -1) {
                    InnerShadow is1 = new InnerShadow();
                    is1.setColor(Color.web("#f13a00"));
                    is1.setRadius(5);
                    is1.setChoke(0.4);
                    playerB.getItemImageView(selectedB[0]).setEffect(is1);
                    clickPlayerB.play();
                }
                if (selectedB[0] != -1 && selectedB[1] != -1) {

                    playerB.shuffleItem(selectedB[0], selectedB[1]);

                    TranslateTransition tt0 = new TranslateTransition(Duration.millis(500), playerB.getItemImageView(selectedB[0]));
                    tt0.setToX(getXPosItem(playerB, selectedB[0]));
                    tt0.setToY(getYPosItem(playerB, selectedB[0]));
                    tt0.play();

                    TranslateTransition tt1 = new TranslateTransition(Duration.millis(500), playerB.getItemImageView(selectedB[1]));
                    tt1.setToX(getXPosItem(playerB, selectedB[1]));
                    tt1.setToY(getYPosItem(playerB, selectedB[1]));
                    tt1.play();

                    for (i = 0; i < 2; i++) {
                        playerB.getItemImageView(selectedB[i]).setEffect(null);
                        selectedB[i] = -1;
                    }
                    clickPlayerB.stop();
                    clickPlayerB = new MediaPlayer(swapSound);
                    clickPlayerB.play();
                    updateScoreBar(calculateFight());
                }
            }

        };

        // ANIMATION FOR A
        Timeline animationSlotA = new Timeline();
        animationSlotA.setCycleCount(60 * (1000 / 50));
        animationSlotA.getKeyFrames().add(
                new KeyFrame(Duration.millis(50),
                        new EventHandler() {

                    @Override
                    public void handle(Event event) {
                        if (playerA.getItemLength() < 9) {
                            for (int i = 0; i < 3; i++) {
                                if (isASpin[i] == true) {
                                    imgView_slotA[i].setImage(slotA.spinStart(i).getImage());
                                }
                            }
                        } else {
                            System.out.println("Player A Start Shuffle");

                            for (int i = 0; i < 3; i++) {
                                pane.getChildren().remove(imgView_slotA[i]);
                            }

                            if (pane.getChildren().contains(imgView_spinA)) {
                                pane.getChildren().remove(imgView_spinA);
                            }

                            pane.getChildren().add(imgView_swapTimeA);
                            TranslateTransition ttIn = new TranslateTransition(Duration.millis(200), imgView_swapTimeA);
                            TranslateTransition ttOut = new TranslateTransition(Duration.millis(200), imgView_swapTimeA);
                            ttIn.setFromX(-300);
                            ttIn.setToX(0);
                            ttIn.setOnFinished(e -> ttOut.play());
                            ttIn.play();
                            ttOut.setFromY(0);
                            ttOut.setToY(-300);
                            ttOut.setDelay(Duration.seconds(1));
                            ttOut.setOnFinished(e -> pane.getChildren().remove(imgView_swapTimeA));

                            animationSlotA.stop();

                            // BlueBG Out
                            //pane.getChildren().remove(imgView_BlueBG);
                            TranslateTransition tt = new TranslateTransition(Duration.millis(500), imgView_BlueBG);
                            tt.setToX(-300);
                            tt.play();
                        }

                        if (!isASpin[0] && !isASpin[1] && !isASpin[2] && !isASpin[3]) {
                            int l = playerA.getItemLength();
                            if (l < 9) {
                                playerA.setItem(slotA.getItem());
                                playerA.setItemImageView(l, new ImageView(playerA.getItem(l).getImage()));
                                pane.getChildren().add(playerA.getItemImageView(l));
                                System.out.println("Player A get item" + l + "(" + playerA.getItem(l).getName() + ")");
                                isASpin[3] = true;
                                pane.getChildren().add(imgView_spinA);

                                // animation
                                TranslateTransition tt = new TranslateTransition(Duration.millis(200), playerA.getItemImageView(l));
                                tt.setFromX(-100);
                                tt.setFromY(-50);
                                tt.setToX(getXPosItem(playerA, l));
                                tt.setToY(getYPosItem(playerA, l));
                                tt.play();
                                ScaleTransition st = new ScaleTransition(Duration.millis(200), playerA.getItemImageView(l));
                                st.setFromX(2);
                                st.setFromY(2);
                                st.setToX(1);
                                st.setToY(1);
                                st.play();
                            }
                        }
                    }
                }));

        // ANIMATION FOR B
        Timeline animationSlotB = new Timeline();
        animationSlotB.setCycleCount(60 * (1000 / 50));
        animationSlotB.getKeyFrames().add(
                new KeyFrame(Duration.millis(50),
                        new EventHandler() {

                    @Override
                    public void handle(Event event) {
                        if (playerB.getItemLength() < 9) {
                            for (int i = 0; i < 3; i++) {
                                if (isBSpin[i] == true) {
                                    imgView_slotB[i].setImage(slotB.spinStart(i).getImage());
                                }
                            }
                        } else {
                            System.out.println("Player B Start Shuffle");
                            for (int i = 0; i < 3; i++) {
                                pane.getChildren().remove(imgView_slotB[i]);
                            }

                            if (pane.getChildren().contains(imgView_spinB)) {
                                pane.getChildren().remove(imgView_spinB);
                            }
                            pane.getChildren().add(imgView_swapTimeB);

                            TranslateTransition ttIn = new TranslateTransition(Duration.millis(200), imgView_swapTimeB);
                            TranslateTransition ttOut = new TranslateTransition(Duration.millis(200), imgView_swapTimeB);
                            ttIn.setFromX(300);
                            ttIn.setToX(0);
                            ttIn.play();
                            ttIn.setOnFinished(e -> ttOut.play());
                            ttOut.setFromY(0);
                            ttOut.setToY(-300);
                            ttOut.setDelay(Duration.seconds(1));
                            ttOut.setOnFinished(e -> pane.getChildren().remove(imgView_swapTimeB));

                            animationSlotB.stop();

                            // RedBG Out
                            //pane.getChildren().remove(imgView_RedBG);
                            TranslateTransition tt = new TranslateTransition(Duration.millis(500), imgView_RedBG);
                            tt.setToX(300);
                            tt.play();

                        }

                        if (!isBSpin[0] && !isBSpin[1] && !isBSpin[2] && !isBSpin[3]) {
                            int l = playerB.getItemLength();
                            if (l < 9) {
                                playerB.setItem(slotB.getItem());
                                playerB.setItemImageView(l, new ImageView(playerB.getItem(l).getImage()));
                                pane.getChildren().add(playerB.getItemImageView(l));
                                System.out.println("Player B get item" + l + "(" + playerB.getItem(l).getName() + ")");
                                isBSpin[3] = true;
                                pane.getChildren().add(imgView_spinB);

                                // animation
                                TranslateTransition tt = new TranslateTransition(Duration.millis(200), playerB.getItemImageView(l));
                                tt.setFromX(100);
                                tt.setFromY(-50);
                                tt.setToX(getXPosItem(playerB, l));
                                tt.setToY(getYPosItem(playerB, l));
                                tt.play();
                                ScaleTransition st = new ScaleTransition(Duration.millis(200), playerB.getItemImageView(l));
                                st.setFromX(2);
                                st.setFromY(2);
                                st.setToX(1);
                                st.setToY(1);
                                st.play();
                            }
                        }
                    }
                }));

        // ANIMATION FOR COUNTER
        ImageView imgTimeLeft = new ImageView();
        pane.getChildren().add(imgTimeLeft);

        Timeline animationCounter = new Timeline();
        animationCounter.setCycleCount(time + 1);
        animationCounter.getKeyFrames().add(
                new KeyFrame(Duration.seconds(1),
                        new EventHandler() {

                    @Override
                    public void handle(Event event) {
                        int timeLeft = Integer.parseInt(counter.getText()) - 1;
                        if (timeLeft > 0) {
                            counter.setText(String.valueOf(timeLeft));
                            if (timeLeft < 10) {
                                imgTimeLeft.setImage(new Image("image/HurryTime/" + String.valueOf(timeLeft) + ".png"));
                                MediaPlayer voicePlayer = new MediaPlayer(secSound);
                                voicePlayer.play();
                                if (pane.getChildren().contains(rectA)) {
                                    pane.getChildren().remove(rectA);
                                }
                                if (pane.getChildren().contains(rectB)) {
                                    pane.getChildren().remove(rectB);
                                }

                            }
                        } else {
                            pane.getChildren().remove(imgTimeLeft);

                            if (playerA.getItemLength() < 9) {
                                for (int l = playerA.getItemLength(); l < 9; l++) {
                                    playerA.setItem(new Grass());
                                    playerA.setItemImageView(l, new ImageView(playerA.getItem(l).getImage()));
                                    pane.getChildren().add(playerA.getItemImageView(l));
                                    playerA.getItemImageView(l).setTranslateX(getXPosItem(playerA, l));
                                    playerA.getItemImageView(l).setTranslateY(getYPosItem(playerA, l));
                                }
                            }
                            if (playerB.getItemLength() < 9) {
                                for (int l = playerB.getItemLength(); l < 9; l++) {
                                    playerB.setItem(new Grass());
                                    playerB.setItemImageView(l, new ImageView(playerB.getItem(l).getImage()));
                                    pane.getChildren().add(playerB.getItemImageView(l));
                                    playerB.getItemImageView(l).setTranslateX(getXPosItem(playerB, l));
                                    playerB.getItemImageView(l).setTranslateY(getYPosItem(playerB, l));
                                }
                            }

                            // START FIGHT
                            //playerA.getItemImageView(selectedA[0]).setEffect(null);
                            //playerB.getItemImageView(selectedB[0]).setEffect(null);
                            pane.getChildren().add(rectA);
                            pane.getChildren().add(rectB);
                            animationCounter.stop();
                            updateScoreBar(0);
                            counter.setText("!!!");
                            animationCounter.stop();
                            scene.setOnKeyPressed(null);
                            startFight();
                        }
                    }
                }));

        ImageView img = new ImageView();
        pane.getChildren().add(img);
        img.toFront();
        timeUse = 3;
        Timeline timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.getKeyFrames().add(
                new KeyFrame(Duration.seconds(1),
                        new EventHandler() {

                    @Override
                    public void handle(Event event) {
                        MediaPlayer voicePlayer;

                        if (timeUse > 0) {
                            img.setImage(new Image("image/Count_" + String.valueOf(timeUse) + ".png"));
                            voicePlayer = new MediaPlayer(new Media(new File("src/sound/SE_Ready_" + String.valueOf(timeUse) + ".mp3").toURI().toString()));
                            voicePlayer.play();
                        } else if (timeUse == 0) {
                            img.setImage(new Image("image/Go!.png"));

                            voicePlayer = new MediaPlayer(voiceStart);
                            voicePlayer.play();

                            animationCounter.play();
                            animationSlotA.play();
                            animationSlotB.play();
                            bgmPlayer.play();
                            scene.setOnKeyPressed(keyHandler);

                        } else {
                            pane.getChildren().remove(img);
                            timeline.stop();
                        }
                        timeUse--;
                    }
                }));
        timeline.playFromStart();

    }

    private void updateScoreBar(int score) {
        System.out.println(score);
        double barChange = 300.0 * score / 6.0;
        //rectA.setWidth(300 - barChange);
        //rectB.setWidth(300 + barChange);
        //rectA.setTranslateX(-150 - barChange / 2);
        //rectB.setTranslateX(150 - barChange / 2);

        TranslateTransition ttA = new TranslateTransition(Duration.millis(200), rectA);
        ttA.setToX(-150 - barChange / 2);
        ttA.play();
        ScaleTransition stA = new ScaleTransition(Duration.millis(200), rectA);
        stA.setToX((300 - barChange) / 300);
        stA.play();

        TranslateTransition ttB = new TranslateTransition(Duration.millis(200), rectB);
        ttB.setToX(150 - barChange / 2);
        ttB.play();
        ScaleTransition stB = new ScaleTransition(Duration.millis(200), rectB);
        stB.setToX((300 + barChange) / 300);
        stB.play();

    }

    ///////////////////
    // FIGHT TIMELINE
    //////////////////
    private void startFight() {
        System.out.println(calculateFight());
        ImageView imgView_Finish = new ImageView(new Image("image/Finish.png"));
        pane.getChildren().add(imgView_Finish);

        MediaPlayer voicePlayer = new MediaPlayer(new Media(new File("src/sound/VE_Finish.mp3").toURI().toString()));
        voicePlayer.play();

        timeUse = 0;
        Timeline fightTimeline = new Timeline();
        fightTimeline.setCycleCount(Timeline.INDEFINITE);
        fightTimeline.getKeyFrames().add(
                new KeyFrame(Duration.seconds(1),
                        new EventHandler() {

                    @Override
                    public void handle(Event event) {
                        if (timeUse == 1) {
                            pane.getChildren().remove(imgView_Finish);
                        }
                        if (timeUse / 1 < 9) {
                            if (timeUse % 1 == 0) {
                                MediaPlayer matchPlayer = new MediaPlayer(swapSound);
                                matchPlayer.play();

                                int round = timeUse / 1;
                                score += fight(playerA.getItem(round), playerB.getItem(round));
                                System.out.println("Round " + round + ": " + score);
                                updateScoreBar(score);

                                TranslateTransition ttA0 = new TranslateTransition(Duration.millis(200), playerA.getItemImageView(round));
                                ttA0.setToX(-100);
                                ttA0.setToY(50);
                                TranslateTransition ttA1 = new TranslateTransition(Duration.millis(200), playerA.getItemImageView(round));
                                ttA1.setToX(playerA.getItemImageView(round).getTranslateX());
                                ttA1.setToY(playerA.getItemImageView(round).getTranslateY());
                                playerA.getItemImageView(round).toFront();

                                ScaleTransition rtA0 = new ScaleTransition(Duration.millis(200), playerA.getItemImageView(round));
                                rtA0.setToX(2);
                                rtA0.setToY(2);
                                ScaleTransition rtA1 = new ScaleTransition(Duration.millis(200), playerA.getItemImageView(round));
                                rtA1.setToX(playerA.getItemImageView(round).getScaleX());
                                rtA1.setToY(playerA.getItemImageView(round).getScaleY());

                                SequentialTransition seqA = new SequentialTransition(
                                        ttA0, rtA0, new PauseTransition(Duration.millis(300)), ttA1, rtA1);
                                seqA.play();

                                TranslateTransition ttB0 = new TranslateTransition(Duration.millis(200), playerB.getItemImageView(round));
                                ttB0.setToX(100);
                                ttB0.setToY(50);
                                TranslateTransition ttB1 = new TranslateTransition(Duration.millis(200), playerB.getItemImageView(round));
                                ttB1.setToX(playerB.getItemImageView(round).getTranslateX());
                                ttB1.setToY(playerB.getItemImageView(round).getTranslateY());
                                playerB.getItemImageView(round).toFront();

                                ScaleTransition rtB0 = new ScaleTransition(Duration.millis(200), playerB.getItemImageView(round));
                                rtB0.setToX(2);
                                rtB0.setToY(2);
                                ScaleTransition rtB1 = new ScaleTransition(Duration.millis(200), playerB.getItemImageView(round));
                                rtB1.setToX(playerB.getItemImageView(round).getScaleX());
                                rtB1.setToY(playerB.getItemImageView(round).getScaleY());

                                SequentialTransition seqB = new SequentialTransition(
                                        ttB0, rtB0, new PauseTransition(Duration.millis(300)), ttB1, rtB1);
                                seqB.play();
                            }
                            timeUse++;
                        } else {
                            ImageView imgView_Winner;

                            EventHandler<KeyEvent> newGameKey = e -> {
                                if (e.getCode() == KeyCode.S || e.getCode() == KeyCode.K || e.getCode() == KeyCode.NUMPAD5) {
                                    System.out.println("New Game Starting !");
                                    pane.getChildren().clear();
                                    scene.setOnKeyPressed(null);
                                    playerA.clearItem();
                                    playerB.clearItem();
                                    bgmPlayer.stop();
                                    startMenu();

                                }
                            };

                            fightTimeline.stop();

                            bgmPlayer.stop();
                            bgmPlayer = new MediaPlayer(finalSound);

                            if (score < 0) {
                                imgView_Winner = new ImageView(new Image("image/BlueWin.png"));
                                pane.getChildren().add(imgView_Winner);
                                scene.setOnKeyPressed(newGameKey);
                                bgmPlayer.play();
                            } else if (score > 0) {
                                imgView_Winner = new ImageView(new Image("image/RedWin.png"));
                                pane.getChildren().add(imgView_Winner);
                                scene.setOnKeyPressed(newGameKey);
                                bgmPlayer.play();
                            } else {
                                bgmPlayer = new MediaPlayer(drawSound);
                                bgmPlayer.play();
                                if (drawCount > 2) {
                                    imgView_Winner = new ImageView(new Image("image/Draw.png"));
                                    pane.getChildren().add(imgView_Winner);
                                    scene.setOnKeyPressed(newGameKey);
                                } else {
                                    drawCount++;
                                    startSlot(5);
                                }
                            }

                        }

                    }
                }));
        fightTimeline.playFromStart();
    }

    private int calculateFight() {
        int sum = 0;
        for (int i = 0; i < 9; i++) {
            sum += fight(playerA.getItem(i), playerB.getItem(i));
        }
        return sum;
    }

    private int fight(Item A, Item B) {
        // RETURN (-) if A wins
        // RETURN (+) if B wins
        if (B.getName().equals(A.getName())) {
            return 0;
        }
        if (A.getWinList().equals(B.getName())) {
            return -A.getDamage();
        }
        if (B.getWinList().equals(A.getName())) {
            return B.getDamage();
        }
        if (A.getName().equals("Shield") && B.getName().equals("Shield")) {
            return 0;
        }
        if (A.getName().equals("Shield")) {
            int dmg = B.getDamage() - A.getDamage();
            if (dmg >= 0) {
                return dmg;
            } else {
                return 0;
            }
        }
        if (B.getName().equals("Shield")) {
            int dmg = -A.getDamage() + B.getDamage();
            if (dmg <= 0) {
                return dmg;
            } else {
                return 0;
            }
        }
        if (A.getName().equals("Torpedo")) {
            return -A.getDamage();
        }
        if (B.getName().equals("Torpedo")) {
            return B.getDamage();
        }
        if (A.getName().equals("Grass")) {
            return B.getDamage();
        }
        if (B.getName().equals("Grass")) {
            return -A.getDamage();
        } else {
            return 0;
        }
    }

    private int getXPosItem(Player player, int i) {
        int x = 80 * (i % 3);
        if (player.equals(playerA)) {
            if (i >= 0 && i <= 2) {
                return (x - 225);
            } else if (i >= 3 && i <= 5) {
                return (x - 217);
            } else if (i >= 6 && i <= 8) {
                return (x - 210);
            }
        } else if (i >= 0 && i <= 2) {
            return (x + 65);
        } else if (i >= 3 && i <= 5) {
            return (x + 60);
        } else if (i >= 6 && i <= 8) {
            return (x + 53);
        }
        return 0;
    }

    private int getYPosItem(Player player, int i) {
        int y = (i / 3) * 80;
        if (player.equals(playerA)) {
            if (i >= 0 && i <= 2) {
                return (y + 190);
            } else if (i >= 3 && i <= 5) {
                return (y + 187);
            } else if (i >= 6 && i <= 8) {
                return (y + 180);
            }
        } else if (i >= 0 && i <= 2) {
            return (y + 190);
        } else if (i >= 3 && i <= 5) {
            return (y + 185);
        } else if (i >= 6 && i <= 8) {
            return (y + 180);
        }
        return 0;
    }

    public static void main(String[] args) {
        launch(args);
    }

}
