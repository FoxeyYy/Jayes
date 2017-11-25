/**
 * Copyright (c) 2011 Michael Kutschke.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Michael Kutschke - initial API and implementation.
 */
package org.eclipse.recommenders.jayes.inference;

import org.eclipse.recommenders.jayes.BayesNet;
import org.eclipse.recommenders.jayes.BayesNodeBase;

import java.util.Map;

public interface IBayesInferer {

    void setNetwork(BayesNet bayesNet);

    void setEvidence(Map<BayesNodeBase, String/*outcome*/> evidence);

    void addEvidence(BayesNodeBase node, String outcome);

    Map<BayesNodeBase, String> getEvidence();

    double[] getBeliefs(BayesNodeBase node);

}
