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
package org.eclipse.recommenders.jayes.sampling;

import org.eclipse.recommenders.jayes.BayesNet;
import org.eclipse.recommenders.jayes.BayesNodeBase;

import java.util.*;

public class BasicSampler implements ISampler {

    private List<BayesNodeBase> topologicallySortedNodes;
    private Map<BayesNodeBase, String> evidence = Collections.emptyMap();
    private Random random = new Random();

    @Override
    public Map<BayesNodeBase, String> sample() {
        Map<BayesNodeBase, String> result = new HashMap<BayesNodeBase, String>();
        result.putAll(evidence);
        for (BayesNodeBase n : topologicallySortedNodes) {
            if (!evidence.containsKey(n)) {
                int newEvidence = sampleOutcome(n, result);
                result.put(n, n.getOutcomeName(newEvidence));
            }
        }
        return result;

    }

    private int sampleOutcome(BayesNodeBase node, Map<BayesNodeBase, String> currentSample) {
        double[] probs = node.marginalize(currentSample);
        double currentProb = 0;
        int newEvidence = 0;
        double rand = random.nextDouble();
        for (double prob : probs) {
            currentProb += prob;
            if (rand < currentProb) {
                break;
            }
            newEvidence++;
        }
        return Math.min(newEvidence, node.getOutcomeCount() - 1);
    }

    @Override
    public void setNetwork(BayesNet net) {
        topologicallySortedNodes = topsort(net.getNodes());
    }

    private List<BayesNodeBase> topsort(List<BayesNodeBase> list) {
        List<BayesNodeBase> result = new LinkedList<BayesNodeBase>();
        Set<BayesNodeBase> visited = new HashSet<BayesNodeBase>();
        for (BayesNodeBase n : list)
            depthFirstSearch(n, visited, result);
        Collections.reverse(result);
        return result;
    }

    private void depthFirstSearch(BayesNodeBase n, Set<BayesNodeBase> visited, List<BayesNodeBase> finished) {
        if (visited.contains(n))
            return;
        visited.add(n);
        for (BayesNodeBase c : n.getChildren())
            depthFirstSearch(c, visited, finished);
        finished.add(n);
    }

    @Override
    public void setEvidence(Map<BayesNodeBase, String> evidence) {
        this.evidence = evidence;

    }

    @Override
    public void seed(long seed) {
        random.setSeed(seed);
    }

    @Override
    @Deprecated
    public void setBN(BayesNet net) {
        setNetwork(net);

    }

}
