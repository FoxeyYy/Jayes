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
package org.eclipse.recommenders.jayes;

import java.util.*;

public class BayesNet {

    private List<BayesNodeBase> nodes = new ArrayList<>();
    private Map<String, BayesNodeBase> nodeMap = new HashMap<>();

    private String name = "Bayesian Network";

    /**
     *
     * @deprecated use createNode instead
     */
    @Deprecated
    public int addNode(BayesNodeBase node) {
        node.setId(nodes.size());
        nodes.add(node);
        if (nodeMap.containsKey(node.getName())) {
            throw new IllegalStateException("Name conflict: " + node.getName() + " already present");
        }
        nodeMap.put(node.getName(), node);
        return node.getId();
    }

    @SuppressWarnings("deprecation")
    public BayesNodeBase createNode(String name, BayesNodeBase.TYPE type) {

        BayesNodeBase node = null;

        switch(type) {
            case DEFAULT:
                node = new BayesNode(name);
                break;
            case NOISY_OR:
                node = new BayesNodeNoisyOR(name);
                break;
        }
        addNode(node);
        return node;
    }

    public BayesNodeBase getNode(String name) {
        return nodeMap.get(name);
    }

    public BayesNodeBase getNode(int id) {
        return nodes.get(id);
    }

    public List<BayesNodeBase> getNodes() {
        return Collections.unmodifiableList(nodes);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
