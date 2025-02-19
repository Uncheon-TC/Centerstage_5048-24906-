package org.firstinspires.ftc.teamcode.drive.FTC2024Final;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.hardware.camera.BuiltinCameraDirection;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.tfod.TfodProcessor;

import java.util.List;

@Autonomous(name = "BlueFar_RP", group = "BlueFar")
public class Autonomous_BF_S_RP_fin extends LinearOpMode {


    int biconPosition = 1;
    private static final boolean USE_WEBCAM = true;

    private static final String TFOD_MODEL_ASSET = "5048Blue.tflite";

    private static final String[] LABELS = {
            "Blue",
    };

    private TfodProcessor tfod;

    private VisionPortal visionPortal;

    double rightopen = 0.5;
    double leftopen = 0.5;

    double rightclose = 0.85;
    double leftclose = 0.15;

    DcMotor armMotor;
    DcMotor grabMotor;
    Servo leftHandServo;
    Servo wristServo;
    Servo rightHandServo;

    public void customSleep(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void aawAdjust(double ArmPower, int ArmTarget, double GrabPower, int GrabTarget, double WristTarget) {

        armMotor.setTargetPosition(ArmTarget);
        grabMotor.setTargetPosition(GrabTarget);
        wristServo.setPosition(WristTarget);

        armMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        grabMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        armMotor.setPower(ArmPower);
        grabMotor.setPower(GrabPower);

        telemetry.addData("armta", ArmTarget);
        telemetry.addData("grta", GrabTarget);

        telemetry.update();
    }

    public void DaawAdjust(double ArmPower, int ArmTarget, double GrabPower, int GrabTarget, double WristTarget, int delay) {

        armMotor.setTargetPosition(ArmTarget);
        grabMotor.setTargetPosition(GrabTarget);
        wristServo.setPosition(WristTarget);

        grabMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        customSleep(delay);
        armMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        armMotor.setPower(ArmPower);
        grabMotor.setPower(GrabPower);

        telemetry.addData("armta", ArmTarget);
        telemetry.addData("grta", GrabTarget);

        telemetry.update();
    }

    public void gripAdjust(double Left_Target, double Right_Target) {
        leftHandServo.setPosition(Left_Target);
        rightHandServo.setPosition(Right_Target);

        telemetry.update();
    }

    @Override
    public void runOpMode() {

        armMotor = hardwareMap.dcMotor.get("ARM");
        grabMotor = hardwareMap.dcMotor.get("grab");
        leftHandServo = hardwareMap.servo.get("leftHand");
        wristServo = hardwareMap.servo.get("wrist");
        rightHandServo = hardwareMap.servo.get("rightHand");

        armMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        grabMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        armMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        grabMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);

        drive.setPoseEstimate(new Pose2d(-38, 65, Math.toRadians(180)));



                //right traj

        Trajectory L1 = drive.trajectoryBuilder(new Pose2d(-38, 65, Math.toRadians(180)))  //to backdrop
                .lineToLinearHeading(new Pose2d(-38, 35, Math.toRadians(0)))

                .addTemporalMarker(0.7, () -> {
                    // Run your action in here!

                    aawAdjust(1, 100, 1, 600, 0.5);

                })

                .build();

        Trajectory L2 = drive.trajectoryBuilder(L1.end())
                .lineToLinearHeading(new Pose2d(-38.5, 15.5, Math.toRadians(0)))

                .addTemporalMarker(0, () -> {
                    gripAdjust(leftclose, rightclose);
                })

                .addTemporalMarker(0, () -> {
                    aawAdjust(1, 0, 1, 0, 0.82);
                })

                .build();

        Trajectory L3 = drive.trajectoryBuilder(L2.end())

                .lineToLinearHeading(new Pose2d(43.5, 15, Math.toRadians(0)))

                .addTemporalMarker(0.2, () -> {
                    // Run your action in here!
                    aawAdjust(1, 0, 1, 0, 0.82);

                })

                .build();

        Trajectory L4 = drive.trajectoryBuilder(L3.end())

                .lineToLinearHeading(new Pose2d(47, 52, Math.toRadians(0)))

                .addTemporalMarker(0.2, () -> {
                    // Run your action in here!
                    aawAdjust(1, 400, 1, 1600, 0.62);

                })
                .build();

        Trajectory L5 = drive.trajectoryBuilder(L4.end())

                .addTemporalMarker(0, () -> {
                    // Run your action in here!
                    DaawAdjust(1, 0, 1, 0, 0.82, 300);


                })

                .addTemporalMarker(0.1, () -> {
                    gripAdjust(leftclose, rightclose);
                })

                .lineToLinearHeading(new Pose2d(40, 16, Math.toRadians(90)))

                .build();

        Trajectory L6 = drive.trajectoryBuilder(L5.end())

                .addTemporalMarker(0, () -> {
                    // Run your action in here!
                    //DaawAdjust(1, 0, 1, 0, 0.82, 100);


                })

                .addTemporalMarker(0.1, () -> {
                    //gripAdjust(leftclose, rightclose);
                })

                .lineToLinearHeading(new Pose2d(57, 16, Math.toRadians(90)))

                .build();






                //mid traj

        Trajectory M1 = drive.trajectoryBuilder(new Pose2d(-38, 65, Math.toRadians(180)))
                .lineToLinearHeading(new Pose2d(-41, 11.5, Math.toRadians(80)))

                .addTemporalMarker(1.5, () -> {
                    // Run your action in here!

                    aawAdjust(0, 0, 1, 700, 0.52);

                })

                .build();


        Trajectory M2 = drive.trajectoryBuilder(M1.end())

                .lineToLinearHeading(new Pose2d(-34, 12, Math.toRadians(0)))

                .addTemporalMarker(0, () -> {
                    gripAdjust(leftclose, rightclose);
                    aawAdjust(1, 0, 1, 0, 0.82);
                })

                .addTemporalMarker(0.5, () -> {
                    // Run your action in here!

                    //aawAdjust(1, 0, 1, 0, 0.82);
                })



                .build();


        Trajectory M3 = drive.trajectoryBuilder(M2.end())

                .addTemporalMarker(0, () -> {
                    // Run your action in here!
                    aawAdjust(1, 0, 1, 0, 0.82);


                })

                .lineToLinearHeading(new Pose2d(43.5, 12, Math.toRadians(0)))

                .build();

        Trajectory M4 = drive.trajectoryBuilder(M3.end())

                .addTemporalMarker(0, () -> {
                    // Run your action in here!
                    aawAdjust(1, 400, 1, 1650, 0.67);
                })

                .lineToLinearHeading(new Pose2d(48, 44, Math.toRadians(0)))

                .build();

        Trajectory M5 = drive.trajectoryBuilder(M4.end())

                .addTemporalMarker(0, () -> {
                    // Run your action in here!
                    DaawAdjust(1, 0, 1, 0, 0.82, 300);


                })

                .addTemporalMarker(0.1, () -> {
                    gripAdjust(leftclose, rightclose);
                })

                .lineToLinearHeading(new Pose2d(42, 16, Math.toRadians(90)))

                .build();

        Trajectory M6 = drive.trajectoryBuilder(M5.end())

                .addTemporalMarker(0, () -> {
                    // Run your action in here!
                    //DaawAdjust(1, 0, 1, 0, 0.82, 100);


                })

                .addTemporalMarker(0.1, () -> {
                    //gripAdjust(leftclose, rightclose);
                })

                .lineToLinearHeading(new Pose2d(57, 16, Math.toRadians(90)))

                .build();


        //left traj

        Trajectory R1 = drive.trajectoryBuilder(new Pose2d(-38, 65, Math.toRadians(180)))
                .lineToLinearHeading(new Pose2d(-38, 20, Math.toRadians(135)))

                .addTemporalMarker(0, () -> {
                    // Run your action in here!

                    aawAdjust(1, 2, 1, 500, 0.5);

                })


                .build();


        Trajectory R2 = drive.trajectoryBuilder(R1.end())
                .addTemporalMarker(0, () -> {
                    // Run your action in here!

                    aawAdjust(1, 0, 1, 0, 0.82);
                })

                .addTemporalMarker(0.5, () -> {
                    gripAdjust(leftclose, rightclose);  //close grip
                })
                .lineToLinearHeading(new Pose2d(-38, 15, Math.toRadians(0)))

                .build();


        Trajectory R3 = drive.trajectoryBuilder(R2.end())
                .lineToLinearHeading(new Pose2d(43.5, 13, Math.toRadians(0)))

                .build();

        Trajectory R4 = drive.trajectoryBuilder((R3.end()))
                .addTemporalMarker(0, () -> {
                    // Run your action in here!

                    aawAdjust(1, 400, 1, 1500, 0.67);
                })

                .addTemporalMarker(0.5, () -> {
                    gripAdjust(leftclose, rightclose);  //close grip
                })

                .lineToLinearHeading(new Pose2d(48, 37, Math.toRadians(0)))


                .build();

        Trajectory R5 = drive.trajectoryBuilder(R4.end())
                .lineToLinearHeading(new Pose2d(43.5, 16, Math.toRadians(90)))

                .addTemporalMarker(0, () -> {
                    // Run your action in here!
                    DaawAdjust(1, 0, 1, 0, 0.82, 100);


                })

                .addTemporalMarker(0.1, () -> {
                            gripAdjust(leftclose, rightclose);
                        })

                .build();

        Trajectory R6 = drive.trajectoryBuilder(R5.end())
                .strafeRight(15)
                .build();






        initTfod();
        while (!isStarted() && !isStopRequested()) {
            // Wait for the DS start button to be touched.
            telemetry.addData("DS preview on/off", "3 dots, Camera Stream");
            telemetry.addData(">", "Touch Play to start OpMode");
            telemetry.addData("position", biconPosition);
            telemetry.update();


            telemetryTfod();

            // Push telemetry to the Driver Station.
            telemetry.update();
        }



                // Share the CPU.
                sleep(20);


                if (biconPosition == 1) {  //code RedC_trajLn

                    leftHandServo.setPosition(leftclose);
                    rightHandServo.setPosition(rightclose);  //init claw close

                    drive.followTrajectory(L1);  //move to backdrop place, extend arm

                    gripAdjust(leftopen, rightclose);  //drop P pixel
                    customSleep(100);

                    drive.followTrajectory(L2);
                    drive.followTrajectory(L3);
                    drive.followTrajectory(L4);

                    gripAdjust(leftclose, rightopen);  //drop Y pixel
                    customSleep(100);


                    drive.followTrajectory(L5);
                    drive.followTrajectory(L6);

                    gripAdjust(leftclose, rightclose);  //close grip






                }
                else if (biconPosition == 2) {  //code RedC_trajMn

                    leftHandServo.setPosition(leftclose);
                    rightHandServo.setPosition(rightclose);  //init claw close

                    drive.followTrajectory(M1);  //move to backdrop place, extend arm

                    gripAdjust(leftopen, rightclose);  //drop P pixel
                    customSleep(100);

                    drive.followTrajectory(M2);

                    drive.followTrajectory(M3);

                    drive.followTrajectory(M4);
                    gripAdjust(leftclose, rightopen);
                    customSleep(500);

                    //gripAdjust(leftopen, rightopen);
                    //customSleep(100);

                    drive.followTrajectory(M5);

                    //gripAdjust(leftclose, rightclose);  //close grip

                    drive.followTrajectory(M6);



                }
                else {  //code RedC_trajRn

                    leftHandServo.setPosition(leftclose);
                    rightHandServo.setPosition(rightclose);  //init claw close


                    drive.followTrajectory(R1);  //move to backdrop place, extend arm

                    gripAdjust(leftopen, rightclose);   //P DR
                    customSleep(100);

                    drive.followTrajectory(R2);


                    drive.followTrajectory(R3);

                    drive.followTrajectory(R4);

                    gripAdjust(leftclose, rightopen);
                    customSleep(100);

                    drive.followTrajectory(R5);

                    drive.followTrajectory(R6);


                }


        visionPortal.close();

    } //run opmode end

    private void initTfod() {

        tfod = new TfodProcessor.Builder()

                .setModelAssetName(TFOD_MODEL_ASSET)
                .setModelLabels(LABELS)

                .build();

        // Create the vision portal by using a builder.
        VisionPortal.Builder builder = new VisionPortal.Builder();

        // Set the camera (webcam vs. built-in RC phone camera).
        if (USE_WEBCAM) {
            builder.setCamera(hardwareMap.get(WebcamName.class, "Webcam 1"));
        } else {
            builder.setCamera(BuiltinCameraDirection.BACK);
        }


        builder.addProcessor(tfod);

        visionPortal = builder.build();

        telemetry.update();
    }

    private void telemetryTfod() {

        biconPosition = 1;

        List<Recognition> currentRecognitions = tfod.getRecognitions();
        telemetry.addData("# Objects Detected", currentRecognitions.size());

        for (Recognition recognition : currentRecognitions) {
            double x = (recognition.getLeft() + recognition.getRight()) / 2;
            double y = (recognition.getTop()  + recognition.getBottom()) / 2;

            if (x > 0 && x < 300) {
                biconPosition = 2;
            } else if (x >= 300) {
                biconPosition = 3;
            } else {
                biconPosition = 1;
            }


            telemetry.addData(""," ");
            telemetry.addData("Image", "%s (%.0f %% Conf.)", recognition.getLabel(), recognition.getConfidence() * 100);
            telemetry.addData("- Position", "%.0f / %.0f", x, y);
            telemetry.addData("biconPosition", biconPosition);


            telemetry.update();


        }


    }
}
