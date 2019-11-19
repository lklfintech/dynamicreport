/*
 * Copyright (c) 2019-2021, LaKaLa.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * https://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.lakala.dynamicreport.common.model;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

/**
 * <p>
 * 自定义分页
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
public class PageImplBean<T> implements Page<T>, Serializable {

    private static final long serialVersionUID = 867755909294344406L;

    private final List<T> content = new ArrayList<T>();

    private final Pageable pageable;

    private final long total;

    public PageImplBean(List<T> content, Pageable pageable, long total) {
    	/*
        if (null == content) {
            throw new IllegalArgumentException("Content must not be null!");
        }*/
    	if(CollectionUtils.isNotEmpty(content)){
    		 this.content.addAll(content);
    	}
        this.total = total;
        this.pageable = pageable;
    }


    public PageImplBean(List<T> content) {
        this(content, null, null == content ? 0 : content.size());
    }


    public int getNumber() {
        return pageable == null ? 0 : pageable.getPageNumber();
    }


    public int getSize() {
        return pageable == null ? 0 : pageable.getPageSize();
    }


    public int getTotalPages() {
        return getSize() == 0 ? 1 : (int) Math.ceil((double) total / (double) getSize());
    }

    public int getNumberOfElements() {
        return content.size();
    }


    public long getTotalElements() {
        return total;
    }

    @Override
    public <U> Page<U> map(Function<? super T, ? extends U> converter) {
        return null;
    }

    public boolean hasPreviousPage() {
        return getNumber() > 0;
    }


    public boolean isFirstPage() {
        return !hasPreviousPage();
    }

    public boolean hasNextPage() {
        return getNumber() + 1 < getTotalPages();
    }


    public boolean isLastPage() {
        return !hasNextPage();
    }


    public Pageable nextPageable() {
        return hasNextPage() ? pageable.next() : null;
    }


    public Pageable previousPageable() {

        if (hasPreviousPage()) {
            return pageable.previousOrFirst();
        }

        return null;
    }


    public Iterator<T> iterator() {
        return content.iterator();
    }


    public List<T> getContent() {
        return Collections.unmodifiableList(content);
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.domain.Page#hasContent()
     */
    public boolean hasContent() {
        return !content.isEmpty();
    }


    public Sort getSort() {
        return pageable == null ? null : pageable.getSort();
    }


    @Override
    public String toString() {

        String contentType = "UNKNOWN";

        if (content.size() > 0) {
            contentType = content.get(0).getClass().getName();
        }

        return String.format("Page %s of %d containing %s instances", getNumber(), getTotalPages(), contentType);
    }


    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }

        if (!(obj instanceof PageImplBean<?>)) {
            return false;
        }

        PageImplBean<?> that = (PageImplBean<?>) obj;

        boolean totalEqual = this.total == that.total;
        boolean contentEqual = this.content.equals(that.content);
        boolean pageableEqual = this.pageable == null ? that.pageable == null : this.pageable.equals(that.pageable);

        return totalEqual && contentEqual && pageableEqual;
    }

    @Override
    public int hashCode() {

        int result = 17;

        result = 31 * result + (int) (total ^ total >>> 32);
        result = 31 * result + (pageable == null ? 0 : pageable.hashCode());
        result = 31 * result + content.hashCode();

        return result;
    }

	@Override
	public boolean isFirst() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isLast() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean hasNext() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean hasPrevious() {
		// TODO Auto-generated method stub
		return false;
	}

//	@Override
	public <S> Page<S> map(Converter<? super T, ? extends S> converter) {
		// TODO Auto-generated method stub
		return null;
	}
}
