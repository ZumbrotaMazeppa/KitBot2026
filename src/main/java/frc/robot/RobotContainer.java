// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import frc.robot.Constants.OperatorConstants;
import static frc.robot.Constants.LemonConstant.*;
import frc.robot.commands.Autos;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.LemonSubsystem;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {
  // The robot's subsystems and commands are defined here...
  private final DriveSubsystem m_DriveSubsystem = new DriveSubsystem();
  private final LemonSubsystem m_LemonSubsystem = new LemonSubsystem();
  // Replace with CommandPS4Controller or CommandJoystick if needed
  private final CommandXboxController m_driverController =
      new CommandXboxController(OperatorConstants.kDriverControllerPort);

  private final SendableChooser<Command> autoChooser = new SendableChooser<>();

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    // Configure the trigger bindings
    configureBindings();
    autoChooser.setDefaultOption("Autonomous", Autos.exampleAuto(m_DriveSubsystem));
  }

  /**
   * Use this method to define your trigger->command mappings. Triggers can be created via the
   * {@link Trigger#Trigger(java.util.function.BooleanSupplier)} constructor with an arbitrary
   * predicate, or via the named factories in {@link
   * edu.wpi.first.wpilibj2.command.button.CommandGenericHID}'s subclasses for {@link
   * CommandXboxController Xbox}/{@link edu.wpi.first.wpilibj2.command.button.CommandPS4Controller
   * PS4} controllers or {@link edu.wpi.first.wpilibj2.command.button.CommandJoystick Flight
   * joysticks}.
   */
  private void configureBindings() {

    m_driverController.leftBumper()
        .whileTrue(m_LemonSubsystem.runEnd(() -> m_LemonSubsystem.intake(), () -> m_LemonSubsystem.stop()));
    // While the right bumper on the operator controller is held, spin up for 0.5
    // second, then launch fuel. When the button is released, stop.
    m_driverController.rightBumper()
        .whileTrue(m_LemonSubsystem.spinUpCommand().withTimeout(SPIN_UP_SECONDS)
            .andThen(m_LemonSubsystem.launchCommand())
            .finallyDo(() -> m_LemonSubsystem.stop()));
    // While the A button is held on the operator controller, eject fuel back out
    // the intake
    m_driverController.a()
        .whileTrue(m_LemonSubsystem.runEnd(() -> m_LemonSubsystem.eject(), () -> m_LemonSubsystem.stop()));

    
  }
  public void configureDrive(){
       if (m_driverController.getRightTriggerAxis() > 0.5) {
        m_DriveSubsystem.driveArcade(() -> -m_driverController.getLeftY() * 0.6, () -> m_driverController.getLeftX() * 0.5);
    } else {
        m_DriveSubsystem.driveArcade(() -> -m_driverController.getLeftY() * 0.80, () -> m_driverController.getLeftX() * 0.65);
    } //Changing these values changes speed, x = turn, y = foward/reverse
  }
  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // An example command will be run in autonomous
    return autoChooser.getSelected();
  }
}
