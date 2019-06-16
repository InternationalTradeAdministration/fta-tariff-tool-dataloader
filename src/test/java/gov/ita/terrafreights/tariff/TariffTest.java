package gov.ita.terrafreights.tariff;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class TariffTest {

  @Test
  public void returns_hsPrefix() {
    Tariff tariff = new Tariff();
    tariff.setTariffLine("123456789");
    assertEquals("1", tariff.getHsPrefix1());
    assertEquals("12", tariff.getHsPrefix2());
    assertEquals("123", tariff.getHsPrefix3());
    assertEquals("1234", tariff.getHsPrefix4());
    assertEquals("12345", tariff.getHsPrefix5());
    assertEquals("123456", tariff.getHsPrefix6());
    assertEquals("1234567", tariff.getHsPrefix7());
    assertEquals("12345678", tariff.getHsPrefix8());
    assertEquals("123456789", tariff.getHsPrefix9());
  }

  @Test
  public void returns_null_hsPrefix_when_tariffLine_is_null() {
    Tariff tariff = new Tariff();
    assertNull(tariff.getHsPrefix1());
    assertNull(tariff.getHsPrefix2());
    assertNull(tariff.getHsPrefix3());
    assertNull(tariff.getHsPrefix4());
    assertNull(tariff.getHsPrefix5());
    assertNull(tariff.getHsPrefix6());
    assertNull(tariff.getHsPrefix7());
    assertNull(tariff.getHsPrefix8());
    assertNull(tariff.getHsPrefix9());
  }

  @Test
  public void returns_null_hsPrefix_when_tariffLine_is_less_than_9_digits() {
    Tariff tariff = new Tariff();
    tariff.setTariffLine("");
    assertNull(tariff.getHsPrefix1());
    assertNull(tariff.getHsPrefix2());
    assertNull(tariff.getHsPrefix3());
    assertNull(tariff.getHsPrefix4());
    assertNull(tariff.getHsPrefix5());
    assertNull(tariff.getHsPrefix6());
    assertNull(tariff.getHsPrefix7());
    assertNull(tariff.getHsPrefix8());
    assertNull(tariff.getHsPrefix9());
  }

}