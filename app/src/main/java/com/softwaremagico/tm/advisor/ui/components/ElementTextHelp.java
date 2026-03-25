package com.softwaremagico.tm.advisor.ui.components;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import com.softwaremagico.tm.Element;
import com.softwaremagico.tm.advisor.R;
import com.softwaremagico.tm.advisor.ui.components.spinner.HelpElement;

public class ElementTextHelp<T extends Element> extends HelpElement<T> {
    private T element;

    public ElementTextHelp(Context context, T element) {
        this(context, null, element);
    }

    public ElementTextHelp(Context context, AttributeSet attrs, T element) {
        super(context, attrs);
        setElement(element);
    }

    @Override
    protected void init(Context context, AttributeSet attrs) {
        inflate(context, R.layout.element_text_help, this);
        initComponents(attrs);
    }

    @Override
    protected void initComponents(AttributeSet attrs) {
        super.initComponents(attrs);
    }

    public void setElement(T element) {
        this.element = element;
        final TextView tagText = findViewById(R.id.translated_tag);
        tagText.setText(element.getName().getTranslatedText());
        if (element.getDescription() == null || element.getDescription().getTranslatedText().isEmpty()) {
            getHelpButton().setVisibility(ImageView.INVISIBLE);
        } else {
            getHelpButton().setVisibility(ImageView.VISIBLE);
        }
        refreshElementColor(element, tagText::setTextColor);
    }

    @Override
    public T getSelection() {
        return element;
    }

    @Override
    public String toString() {
        return element != null ? element.toString() : "null";
    }

}
