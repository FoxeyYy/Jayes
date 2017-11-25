package org.eclipse.recommenders.jayes;

import org.eclipse.recommenders.internal.jayes.util.BidirectionalMap;
import org.eclipse.recommenders.jayes.factor.AbstractFactor;
import org.eclipse.recommenders.jayes.factor.DenseFactor;
import org.eclipse.recommenders.jayes.util.MathUtils;

import java.util.*;

public abstract class BayesNodeBase {

    public enum TYPE {
        DEFAULT, NOISY_OR
    }

    protected final String name;
    private final List<BayesNodeBase> children = new ArrayList<>();
    private final BidirectionalMap<String, Integer> outcomeIndices = new BidirectionalMap<>();
    protected final AbstractFactor factor = new DenseFactor();
    private final List<String> outcomesList = new ArrayList<>();
    protected List<BayesNodeBase> parents = new ArrayList<>();
    private int outcomes = 0;
    private int id = -1;

    public BayesNodeBase(String name) {
        this.name = name;
    }

    /**
     * Must be called after the parents and outcomes, and the outcome of the parents are set.
     */
    abstract public void setProbabilities(final double... probabilities);

    final public double[] getProbabilities() {
        return factor.getValues().toDoubleArray();
    }

    final public List<BayesNodeBase> getChildren() {
        return children;
    }

    final public List<BayesNodeBase> getParents() {
        return Collections.unmodifiableList(parents);
    }

    final public void setParents(final List<BayesNodeBase> parents) {
        for (BayesNodeBase oldParent : this.parents) {
            oldParent.children.remove(this);
        }
        this.parents = parents;
        for (BayesNodeBase p : parents) {
            p.children.add(this);
        }
        adjustFactordimensions();
    }

    protected final void adjustFactordimensions() {
        final int[] dimensions = new int[parents.size() + 1];
        final int[] dimensionIds = new int[parents.size() + 1];
        fillWithParentDimensions(dimensions, dimensionIds);
        insertSelf(dimensions, dimensionIds);
        factor.setDimensions(dimensions);
        factor.setDimensionIDs(dimensionIds);
    }

    final private void insertSelf(final int[] dimensions, final int[] dimensionIds) {
        dimensions[dimensions.length - 1] = getOutcomeCount();
        dimensionIds[dimensionIds.length - 1] = getId();
    }

    final private void fillWithParentDimensions(final int[] dimensions, final int[] dimensionIds) {
        for (ListIterator<BayesNodeBase> it = parents.listIterator(); it.hasNext();) {
            final BayesNodeBase p = it.next();
            dimensions[it.nextIndex() - 1] = p.getOutcomeCount();
            dimensionIds[it.nextIndex() - 1] = p.getId();
        }
    }

    public double[] marginalize(final Map<BayesNodeBase, String> evidence) {
            for (final BayesNodeBase p : parents) {
                if (evidence.containsKey(p)) {
                    factor.select(p.getId(), p.getOutcomeIndex(evidence.get(p)));
                } else {
                    factor.select(p.getId(), -1);
                }
            }
            final double[] result = MathUtils.normalize(factor.marginalizeAllBut(-1));
            factor.resetSelections();

            return result;
    }

    final public int getId() {
        return id;
    }

    /**
     * @deprecated internal method, don't use. visibility might change to default
     */
    @Deprecated
    final public void setId(final int id) {
        if (this.id != -1) {
            throw new IllegalStateException("Impossible to reset Id!");
        }
        if (id < 0) {
            throw new IllegalArgumentException("id has to be greater or equal to 0");
        }
        this.id = id;

    }

    final public void addOutcomes(String... names) {
        if (!Collections.disjoint(outcomesList, Arrays.asList(names))) {
            throw new IllegalArgumentException("Outcome already exists");
        }
        for (String name : names) {
            outcomeIndices.put(name, outcomes);
            outcomes++;
            outcomesList.add(name);
        }
        adjustFactordimensions();
    }

    final public int addOutcome(final String name) {
        addOutcomes(name);
        return outcomes - 1;
    }

    final public int getOutcomeIndex(final String name) {
        try {
            return outcomeIndices.get(name);
        } catch (NullPointerException ex) {
            throw new IllegalArgumentException(name, ex);
        }
    }

    final public String getOutcomeName(final int index) {
        return outcomeIndices.getKey(index);
    }

    final public int getOutcomeCount() {
        return outcomes;
    }

    final public AbstractFactor getFactor() {
        return factor;
    }

    final public List<String> getOutcomes() {
        return Collections.unmodifiableList(outcomesList);
    }

    final public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
