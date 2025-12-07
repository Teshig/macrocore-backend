package org.grimjo.macrocore.game.model;

import java.util.List;

public interface Policy {
  List<Decree> evaluate(Settlement settlement);
}
