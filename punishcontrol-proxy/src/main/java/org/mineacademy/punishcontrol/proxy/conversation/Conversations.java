package org.mineacademy.punishcontrol.proxy.conversation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.experimental.UtilityClass;

@UtilityClass
@SuppressWarnings("ALL")
public class Conversations {

  private final List<OneTimeConversation> conversation = new ArrayList<>();


  public List<OneTimeConversation> conversation(){

    return Collections.unmodifiableList(conversation);
  }

  public void register(final OneTimeConversation conv) {
    conversation.add(conv);
  }
}
