package com.kuaishou.business.core.identity.product;

import com.kuaishou.business.core.identity.MatchResult;

/**
 * 水平产品定义
 *
 * 需要结合{@link KProduct}或其扩展使用
 */
public interface NormalProductIdentityDefinition<T> extends ProductIdentityDefinition<T, MatchResult> {

    @Override
    MatchResult match(T request);

}
