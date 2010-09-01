package org.mst.acm.soda;

/**
 * Class for storing info about a soda can.
 * Stores the name of the soda and the number of cans
 * available.
 * @author nathan
 *
 */
public class SodaCan
{
  private String m_name;
  private int m_depth;
  private int m_price;
  
  /**
   * Creates a new soda can
   * @param name Name of soda
   * @param price Price of soda
   * @param depth Number of cans
   */
  public SodaCan(String name, int price, int depth)
  {
    m_name = name;
    m_price = price;
    m_depth = depth;
  }

  /**
   * Creates a new soda can with a default depth of 0
   * @param name Name of soda
   * @param price Price of soda
   */
  public SodaCan(String name, int price)
  {
    m_name = name;
    m_price = price;
    m_depth = 0;
  }

  /**
   * @return The name of the soda
   */
  public String name()
  {
    return m_name;
  }

  /**
   * Sets the name of the soda
   * @param name soda name
   */
  public void name(String name)
  {
    m_name = name;
  }

  /**
   * @return the price of the soda
   */
  public int price()
  {
    return m_price;
  }

  /**
   * Sets the price of the soda
   * @param price the price of the soda
   */
  public void price(int price)
  {
    m_price = price;
  }

  /**
   * @return the number of sodas
   */
  public int depth()
  {
    return m_depth;
  }

  /**
   * sets the number of sodas
   * @param depth number of sodas
   */
  public void depth(int depth)
  {
    m_depth = depth;
  }
}
