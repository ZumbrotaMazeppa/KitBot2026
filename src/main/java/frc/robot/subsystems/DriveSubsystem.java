// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;



import java.util.function.DoubleSupplier;
import com.revrobotics.ResetMode;
import com.revrobotics.PersistMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkMaxConfig;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import static frc.robot.Constants.DriveConstants.*;


public class DriveSubsystem extends SubsystemBase {
  /** Creates a new ExampleSubsystem. */
  private final SparkMax leftLeader;
  private final SparkMax leftFollower;
  private final SparkMax rightLeader;
  private final SparkMax rightFollower;

  private final DifferentialDrive drive;

  public DriveSubsystem() {

  leftLeader = new SparkMax(1, MotorType.kBrushless);
  leftFollower = new SparkMax(3, MotorType.kBrushless);
  rightLeader = new SparkMax(2, MotorType.kBrushless);
  rightFollower = new SparkMax(4, MotorType.kBrushless);

  drive = new DifferentialDrive(leftLeader, rightLeader);

  leftLeader.setCANTimeout(250);
  rightLeader.setCANTimeout(250);
  leftFollower.setCANTimeout(250);
  rightFollower.setCANTimeout(250);

  SparkMaxConfig config = new SparkMaxConfig();
  config.voltageCompensation(12);
  config.smartCurrentLimit(DRIVE_MOTOR_CURRENT_LIMIT);

  config.follow(leftLeader);
  leftFollower.configure(config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
  config.follow(rightLeader);
  rightFollower.configure(config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

  config.disableFollowerMode();
  rightLeader.configure(config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

  config.inverted(true);
  leftLeader.configure(config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
  public Command driveArcade(DoubleSupplier xSpeed, DoubleSupplier zRotation) {
    return this.run(
        () -> drive.arcadeDrive(xSpeed.getAsDouble(), zRotation.getAsDouble()));
  }
}