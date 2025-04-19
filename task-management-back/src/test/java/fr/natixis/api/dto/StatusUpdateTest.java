package fr.natixis.api.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class StatusUpdateTest {
    @Test
    void shouldValidateCompletedStatus() {
        StatusUpdate update = new StatusUpdate();
        assertFalse(update.isCompleted());

        update.setCompleted(true);
        assertTrue(update.isCompleted());
    }
}