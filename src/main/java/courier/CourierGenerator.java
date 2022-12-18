package courier;

import org.apache.commons.lang3.RandomStringUtils;

public class CourierGenerator {
    public Courier getCourierDefault() {
        return new Courier("ninja", "1234", "saske");
    }
    public Courier getCourierRandom() {
        return new Courier(RandomStringUtils.randomAlphanumeric(5), RandomStringUtils.randomAlphanumeric(4), RandomStringUtils.randomAlphanumeric(5));
    }
}

