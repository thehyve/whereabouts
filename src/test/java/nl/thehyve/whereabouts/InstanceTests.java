package nl.thehyve.whereabouts;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import nl.thehyve.whereabouts.domains.Instance;
import nl.thehyve.whereabouts.dto.InstanceRepresentation;
import org.junit.Test;

public class InstanceTests {

    @Test
    public void testInstanceRepresentationEquals() {
        EqualsVerifier
            .forClass(InstanceRepresentation.class)
            .suppress(Warning.STRICT_INHERITANCE, Warning.NONFINAL_FIELDS)
            .verify();
    }

    @Test
    public void testInstanceDomainEquals() {
        EqualsVerifier
            .forClass(Instance.class)
            .suppress(Warning.SURROGATE_KEY)
            .verify();
    }

    @Test(expected = NullPointerException.class)
    public void testInstanceDomainWithInvalidAddress() {
        // Use invalid data for instance
        new Instance(null, "Test query");
    }

    @Test(expected = NullPointerException.class)
    public void testInstanceDomainWithInvalidSourceQuery() {
        // Use invalid data for instance
        new Instance("Address", null);
    }

}
