/*
 * Copyright © 2013 <code@io7m.com> http://io7m.com
 * 
 * Permission to use, copy, modify, and/or distribute this software for any
 * purpose with or without fee is hereby granted, provided that the above
 * copyright notice and this permission notice appear in all copies.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
 * WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY
 * SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
 * WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
 * ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR
 * IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */

package com.io7m.jcanephora.bluecafe;

import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.io7m.blueberry.TestReportConfig;
import com.io7m.blueberry.gui.GUI;
import com.io7m.blueberry.gui.GUIProjectInfo;
import com.io7m.blueberry.gui.GUIProjectVersion;

public final class BlueCafe
{
  public static void main(
    final String[] args)
    throws URISyntaxException
  {
    final TestReportConfig xml_config = new TestReportConfig();
    xml_config.setOutputEnvironment(true);
    xml_config.setOutputSystemProperties(true);

    final GUIProjectVersion version = new GUIProjectVersion(0, 29, 0, null);
    final GUIProjectInfo info = new GUIProjectInfo("bluecafe", version);
    info.addPackagePrefix("com.io7m");
    info.setProjectURI(new URI("http://io7m.com/software/jcanephora"));
    info.setProjectIcon(BlueCafe.class.getResource(
      "/com/io7m/jcanephora/bluecafe/bluecafe.png").toURI());

    SwingUtilities.invokeLater(new Runnable() {
      @Override public void run()
      {
        final GUI g = new GUI(info, xml_config);
        g.getMainWindow().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      }
    });
  }
}
