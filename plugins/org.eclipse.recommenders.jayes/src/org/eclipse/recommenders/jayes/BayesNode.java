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

import org.eclipse.recommenders.jayes.factor.arraywrapper.DoubleArrayWrapper;
import org.eclipse.recommenders.jayes.util.MathUtils;

public class BayesNode extends BayesNodeBase {


    public BayesNode(String name) {
        super(name);
    }

    @Override
    public void setProbabilities(double... probabilities) {
        adjustFactordimensions();
        if (probabilities.length != MathUtils.product(factor.getDimensions())) {
            throw new IllegalArgumentException("Probability table does not have expected size. Expected: "
                    + MathUtils.product(factor.getDimensions()) + " but got: " + probabilities.length);
        }
        factor.setValues(new DoubleArrayWrapper(probabilities));
    }

}
