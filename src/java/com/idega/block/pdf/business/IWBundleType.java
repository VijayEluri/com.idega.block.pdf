/*
 * $Id: IWBundleType.java,v 1.4 2006/04/09 11:41:28 laddi Exp $
 * Created on 3.11.2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package com.idega.block.pdf.business;

import java.util.Map;

import org.ujac.util.exi.BaseExpressionOperation;
import org.ujac.util.exi.ExpressionContext;
import org.ujac.util.exi.ExpressionException;
import org.ujac.util.exi.ExpressionInterpreter;
import org.ujac.util.exi.ExpressionOperation;
import org.ujac.util.exi.ExpressionTuple;
import org.ujac.util.exi.NoOperandException;
import org.ujac.util.exi.Operand;
import org.ujac.util.exi.type.BaseType;
import org.ujac.util.text.FormatHelper;

import com.idega.idegaweb.IWBundle;

/**
 * 
 *  Last modified: $Date: 2006/04/09 11:41:28 $ by $Author: laddi $
 * 
 * @author <a href="mailto:aron@idega.com">aron</a>
 * @version $Revision: 1.4 $
 */
public class IWBundleType extends BaseType {
    
	protected org.ujac.util.exi.ExpressionInterpreter interpreter;
	
    /**
     * The 'get' operation for ResourceBundles.
     */
    class GetOperation extends BaseExpressionOperation {
    	
      public Object evaluate(ExpressionTuple expr, ExpressionContext arg1) throws ExpressionException {
        Operand operand = expr.getOperand();
        if (operand == null) {
          throw new NoOperandException("No operand given for operation: " + expr.getOperation() + " on object " + expr.getObject() + "!");         
        }
        // getting operand
        String operandValue = IWBundleType.this.interpreter.evalStringOperand(operand, arg1);

        IWBundle bundle = (IWBundle) (expr.getObject().getValue());
        String s =  bundle.getProperty(operandValue);
        return s;
			}

			/**
       * Evaluates the given values.
       * @param expr The expression tuple to process. 
       * @param params The map holding the parameters.
       * @param bean The bean used to retrieve parameter values, if the parameter value didn't exist in the params map.
       * @param formatHelper The format helper to use.
       * @return The result of the tuple evaluation.
       * @exception ExpressionException If the evaluation failed.
       */
      public Object evaluate(ExpressionTuple expr, Map params, Object bean, FormatHelper formatHelper)
        throws ExpressionException {
          
        Operand operand = expr.getOperand();
        if (operand == null) {
          throw new NoOperandException("No operand given for operation: " + expr.getOperation() + " on object " + expr.getObject() + "!");         
        }
        // getting operand
        String operandValue = IWBundleType.this.interpreter.evalStringOperand(operand, new ExpressionContext(params, bean, formatHelper));

        IWBundle bundle = (IWBundle) (expr.getObject().getValue());
        String s =  bundle.getProperty(operandValue);
        return s;
      }

      /**
       * Gets a description for the operation.
       * @return The item's description.
       */
      public String getDescription() {
        return "Gets an element from the bundle by its name.";
      }
    }

    	
    /**
     * @param interpreter
     */
    public IWBundleType(ExpressionInterpreter interpreter) {
        super(interpreter);
        this.interpreter = interpreter;
        ExpressionOperation op = new GetOperation();
        addOperation(".", op);
        addOperation("[]", op);
        addOperation("get", op);
    }
    /* (non-Javadoc)
     * @see org.ujac.util.exi.ExpressionType#getType()
     */
    public Class getType() {
        return IWBundle.class;
        
    }
    
    
}
