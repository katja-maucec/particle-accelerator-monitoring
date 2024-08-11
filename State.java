/**
 * Program begins with an OFF state.
 * 
 * ON: Puts the sensor on standby before it can start measuring.
 * MEASURING: Begins data transmission.
 * IDLE: Measuring has been paused and needs to be manually resumed.
 * OFF: The sensor is fully shut off.
 */
enum State {
    ON,
    MEASURING,
    IDLE,
    OFF
}
