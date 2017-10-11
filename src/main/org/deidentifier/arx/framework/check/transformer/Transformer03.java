/*
 * ARX: Powerful Data Anonymization
 * Copyright 2012 - 2017 Fabian Prasser, Florian Kohlmayer and contributors
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.deidentifier.arx.framework.check.transformer;

import org.deidentifier.arx.ARXConfiguration.ARXConfigurationInternal;
import org.deidentifier.arx.framework.check.distribution.IntArrayDictionary;
import org.deidentifier.arx.framework.data.DataMatrix;
import org.deidentifier.arx.framework.data.GeneralizationHierarchy;

/**
 * The class Transformer03.
 * 
 * @author Fabian Prasser
 * @author Florian Kohlmayer
 */
public class Transformer03 extends AbstractTransformer {

    /**
     * Instantiates a new transformer.
     *
     * @param data the data
     * @param hierarchies the hierarchies
     * @param otherValues
     * @param dictionarySensValue
     * @param dictionarySensFreq
     * @param config
     */
    public Transformer03(final DataMatrix data,
                         final GeneralizationHierarchy[] hierarchies,
                         final DataMatrix otherValues,
                         final IntArrayDictionary dictionarySensValue,
                         final IntArrayDictionary dictionarySensFreq,
                         final ARXConfigurationInternal config) {
        super(data, hierarchies, otherValues, dictionarySensValue, dictionarySensFreq, config);
    }

    @Override
    protected void processAll() {
        for (int i = startIndex; i < stopIndex; i++) {
            // Transform
            buffer.setRow(i);
            data.setRow(i);
            buffer.setValueAtColumn(index0, idindex0[data.getValueAtColumn(index0)][generalizationindex0]);
            buffer.setValueAtColumn(index1, idindex1[data.getValueAtColumn(index1)][generalizationindex1]);
            buffer.setValueAtColumn(index2, idindex2[data.getValueAtColumn(index2)][generalizationindex2]);

            // Call
            delegate.callAll(i, i);
        }
    }

    @Override
    protected void processGroupify() {

        while (element != null) {

            // Transform
            buffer.setRow(element.representative);
            data.setRow(element.representative);
            buffer.setValueAtColumn(index0, idindex0[data.getValueAtColumn(index0)][generalizationindex0]);
            buffer.setValueAtColumn(index1, idindex1[data.getValueAtColumn(index1)][generalizationindex1]);
            buffer.setValueAtColumn(index2, idindex2[data.getValueAtColumn(index2)][generalizationindex2]);

            // Call
            delegate.callGroupify(element.representative, element);

            // Next element
            element = element.nextOrdered;
        }
    }

    @Override
    protected void processSnapshot() {

        startIndex *= ssStepWidth;
        stopIndex *= ssStepWidth;

        for (int i = startIndex; i < stopIndex; i += ssStepWidth) {
            // Transform
            buffer.setRow(snapshot[i]);
            data.setRow(snapshot[i]);
            buffer.setValueAtColumn(index0, idindex0[data.getValueAtColumn(index0)][generalizationindex0]);
            buffer.setValueAtColumn(index1, idindex1[data.getValueAtColumn(index1)][generalizationindex1]);
            buffer.setValueAtColumn(index2, idindex2[data.getValueAtColumn(index2)][generalizationindex2]);

            // Call
            delegate.callSnapshot(snapshot[i], snapshot, i);
        }
    }
}
