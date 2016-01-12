/******************************************************************************
 * Copyright (C) 2010-2016 CERN. All rights not expressly granted are reserved.
 * 
 * This file is part of the CERN Control and Monitoring Platform 'C2MON'.
 * C2MON is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the license.
 * 
 * C2MON is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for
 * more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with C2MON. If not, see <http://www.gnu.org/licenses/>.
 *****************************************************************************/
package cern.c2mon.client.jviews.adminmessage;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.JTableHeader;

import cern.c2mon.client.common.admin.BroadcastMessage;
import cern.c2mon.client.common.admin.BroadcastMessageImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A panel for displaying all AdminMessages that were received since the TIM
 * Viewer was started.
 * @author J. Stowisek
 */
public class BroadcastMessageHistoryPanel extends JPanel {
  /**
   * Serial Version UID for the BroadcastMessageHistoryPanel class
   */
  private static final long serialVersionUID = -3322922665656938973L;
  private JLabel labelHeader = new JLabel();
  private JTable tableMessages = new JTable();
  private JTableHeader jTableHeader1 = new JTableHeader();
  private static final Logger LOG = LoggerFactory.getLogger(BroadcastMessageHistoryPanel.class);

  private static final int TIMESTAMP_COLUMN = 0;
  private static final int MESSAGE_CONTENT_COLUMN = 1;
  private static final int AUTHOR_COLUMN = 2;

  public BroadcastMessageHistoryPanel(Collection<BroadcastMessage> messages) {
    this.setLayout(null);
    Dimension d = new Dimension(400, 200);

    this.setSize(d);
    this.setPreferredSize(d);
    this.setMaximumSize(d);
    this.setMaximumSize(d);
    labelHeader.setText("<HTML><BODY><P>The following administrator messages were received:</P></BODY></HTML>");
    labelHeader.setBounds(new Rectangle(5, 5, 390, 20));
    tableMessages.setBounds(new Rectangle(5, 25, 390, 170));
    tableMessages.setModel(new MessageTableModel(messages));
    this.add(tableMessages, null);
    this.add(labelHeader, null);

    tableMessages.addMouseListener(new MouseAdapter() {
      public void mouseClicked(final MouseEvent e) {
        if (e.getClickCount() == 2) {
          int row = tableMessages.getSelectedRow();
          final String message = (String) tableMessages.getModel().getValueAt(row, 1);
          final String author = (String) tableMessages.getModel().getValueAt(row, 2);
          showMessage(message, author);
        }
      }
    });
    //addTestMessages(); // only for debbuging
  }

  public void setMessages(final Collection<BroadcastMessage> pMessages) {
    if (pMessages == null) {
      return;
    }
    tableMessages.setModel(new MessageTableModel(pMessages));    
  }

  class MessageTableModel extends AbstractTableModel {
    /**
     * Serial Version UID for the MessageTableModel class
     */
    private static final long serialVersionUID = -5507412590528170978L;
    private BroadcastMessage[] messages = {};
    private DateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm");

    private String[] column_names = {"Received at", "Message text", "Sent by"};

    public MessageTableModel(final Collection<BroadcastMessage> pMessages) {
      this.messages = pMessages.toArray(new BroadcastMessage[0]);
    }

    public  int getColumnCount() {
      if (messages.length == 0) {
        return 1;
      }
      else {
        return column_names.length;
      }
    }

    public int getRowCount() {
      if (messages.length == 0) {
        return 1;
      }
      else {
        return this.messages.length;
      }
    }

    public String getColumnName(int columnIndex) { 
      if (messages.length == 0) {
        return "No messages were received yet";
      }
      else {
        return this.column_names[columnIndex];
      }
    }

    public boolean isCellEditable(int rowIndex, int columnIndex) {
      return false;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
      if (messages.length == 0) {
        return "No messages have been received yet.";
      }
      BroadcastMessage msg = messages[rowIndex];
      switch(columnIndex) {
        case TIMESTAMP_COLUMN: 
          return format.format(msg.getTimestamp());
        case MESSAGE_CONTENT_COLUMN: return msg.getMessage();
        case AUTHOR_COLUMN:
          if (msg.getSender() != null) {
            return msg.getSender();
          }
          else {
            return "An anonymous hacker";
          }
        default: return "";
      }
    }
  }
  
  /**
   * Displays a message in its own dialog box.
   * This is useful as some messages are very long
   * and cannot fit in the History Panel.
   * @param message The message to be displayed
   * @param author The author of the message
   */
  private void showMessage(final String message, final String author) {
    
    JOptionPane.showMessageDialog(null,
        message.replace(". ", ".\n"),
        "[" + author + "]",
        JOptionPane.INFORMATION_MESSAGE);
  }

  /**
   * Used only for testing.
   * Adds a test message in the list of messages.
   */
  private void addTestMessages() {

    BroadcastMessage m1 = new BroadcastMessageImpl(BroadcastMessage.BroadcastMessageType.WARN
        , "Anonymous Hacker"
        , "Hi Guys!"
        , new Timestamp(System.currentTimeMillis()));

    BroadcastMessage m2 = new BroadcastMessageImpl(BroadcastMessage.BroadcastMessageType.WARN
        , "Anonymous Hacker who likes writing long messages"
        , "This is a super long message... It's just here to test " 
        + "that super long messages can be seen properly.. \n" 
        + "Otherwise it's of no use and noone really cares about it."
        , new Timestamp(System.currentTimeMillis()));
    
    Collection<BroadcastMessage> testMessages = new ArrayList<BroadcastMessage>();
    testMessages.add(m1);
    testMessages.add(m2);

    setMessages(testMessages);
  }
}