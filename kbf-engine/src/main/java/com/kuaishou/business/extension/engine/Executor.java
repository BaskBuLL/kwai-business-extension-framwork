package com.kuaishou.business.extension.engine;

import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

import org.apache.commons.collections4.CollectionUtils;

import com.google.common.collect.Lists;
import com.kuaishou.business.core.context.ExecuteContext;
import com.kuaishou.business.core.extpoint.ExtPoint;
import com.kuaishou.business.core.function.ExtCallback;
import com.kuaishou.business.core.identity.manage.KbfRealizeItem;
import com.kuaishou.business.core.reduce.ReduceType;
import com.kuaishou.business.core.reduce.Reducer;
import com.kuaishou.business.core.session.KSessionScope;

public abstract class Executor<Context extends ExecuteContext> {

	public <Ext extends ExtPoint, T, R, P> R execute(Class<Ext> extClz, String methodName, ExtCallback<Ext, T> extMethod, Supplier<T> defaultMethod,
		Reducer<T, R> reducer, P request) {
		Context context = buildExecutorContext(extClz, methodName, request);
		if (!check(context)) {
			return reducer.reduce(Lists.newArrayList(defaultMethod.get()));
		}

		Collection<KbfRealizeItem> currentProducts = recognize(context);

		List<KbfRealizeItem> kbfRealizeItemList = Lists.newArrayList();
		kbfRealizeItemList.add(KSessionScope.getCurrentBusiness());
		kbfRealizeItemList.addAll(currentProducts);

		List<Ext> instanceList = SimpleKExtPointManger.getInstance(kbfRealizeItemList, extClz, methodName);

		return execAndHandlerResults(extMethod, defaultMethod, reducer, instanceList);
	}

	protected static <Ext extends ExtPoint, T, R> R execAndHandlerResults(ExtCallback<Ext, T> extMethod,
		Supplier<T> defaultMethod, Reducer<T, R> reducer, List<Ext> instanceList) {
		if (CollectionUtils.isEmpty(instanceList)) {
			return reducer.reduce(Lists.newArrayList(defaultMethod.get()));
		}
		List<T> results = Lists.newArrayList();
		boolean reduceFirst = ReduceType.FIRST.equals(reducer.reduceType());
		for (Ext instance : instanceList) {
			T result = extMethod.apply(instance);
			if (reduceFirst) {
				if (reducer.predicate(result)) {
					return reducer.reduce(Lists.newArrayList(result));
				}
			} else {
				results.add(result);
			}
		}
		T defaultMethodResult = defaultMethod.get();
		results.add(defaultMethodResult);
		return reducer.reduce(results);
	}

	public abstract <Ext extends ExtPoint, P> Context buildExecutorContext(Class<Ext> extClz, String methodName, P request);

	public abstract <Product extends KbfRealizeItem> Collection<Product> recognize(Context context);

	public abstract boolean check(Context context);

}
