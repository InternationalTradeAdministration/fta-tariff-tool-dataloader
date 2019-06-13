package gov.ita.terrafreights.tariff;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TariffTest {

  private Tariff tariff;

  @Before
  public void set_up() {
    tariff = new Tariff();
    tariff.setTariffLine("123456789");
  }

  @Test
  public void returns_hsPrefix1() {
    assertEquals("1", tariff.getHsPrefix1());
  }

  @Test
  public void returns_hsPrefix2() {
    assertEquals("2", tariff.getHsPrefix2());
  }

  @Test
  public void returns_hsPrefix3() {
    assertEquals("3", tariff.getHsPrefix3());
  }

  @Test
  public void returns_hsPrefix4() {
    assertEquals("4", tariff.getHsPrefix4());
  }

  @Test
  public void returns_hsPrefix5() {
    assertEquals("5", tariff.getHsPrefix5());
  }

  @Test
  public void returns_hsPrefix6() {
    assertEquals("6", tariff.getHsPrefix6());
  }

  @Test
  public void returns_hsPrefix7() {
    assertEquals("7", tariff.getHsPrefix7());
  }

  @Test
  public void returns_hsPrefix8() {
    assertEquals("8", tariff.getHsPrefix8());
  }

  @Test
  public void returns_hsPrefix9() {
    assertEquals("9", tariff.getHsPrefix9());
  }

}