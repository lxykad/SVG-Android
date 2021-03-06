package com.github.megatronking.svg.applet.ui;

import com.github.megatronking.svg.applet.support.VectorFileFilter;

import java.awt.BorderLayout;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

class VectorEditorPanel extends JPanel implements VectorContentViewer.OnTextWatcher {

    private String mData;
    private String mName;

    private VectorImageViewer mImageViewer;

    private MainFrame mMainFrame;

    VectorEditorPanel(MainFrame frame, String data, String name) {
        this.mData = data;
        this.mName = name;
        this.mMainFrame = frame;

        setOpaque(false);
        BorderLayout borderLayout = new BorderLayout();
        setLayout(borderLayout);

        buildVectorViewer();
    }

    private void buildVectorViewer() {
        JPanel panel = new JPanel(new BorderLayout());

        JSplitPane splitter = new JSplitPane();
        splitter.setContinuousLayout(true);
        splitter.setResizeWeight(0.75);
        splitter.setBorder(null);

        VectorContentViewer contentViewer = new VectorContentViewer(mData, this);
        JScrollPane scroller = new JScrollPane(contentViewer);
        scroller.setOpaque(false);
        scroller.setBorder(null);
        scroller.getViewport().setBorder(null);
        scroller.getViewport().setOpaque(false);
        splitter.setLeftComponent(scroller);

        mImageViewer = new VectorImageViewer(mData);
        splitter.setRightComponent(mImageViewer);

        panel.add(splitter, BorderLayout.CENTER);
        add(panel);
    }

    File chooseSaveFile(boolean create, File defaultSave) {
        if (!create) {
            return new File(mName);
        }
        JFileChooser chooser = new JFileChooser(mName.substring(0, mName.lastIndexOf(File.separatorChar)));
        chooser.setFileFilter(new VectorFileFilter());
        if (defaultSave != null) {
            chooser.setSelectedFile(defaultSave);
        }
        int choice = chooser.showSaveDialog(this);
        if (choice == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            if (!file.getAbsolutePath().endsWith(".xml")) {
                mName = file.getAbsolutePath();
                return new File(mName);
            }
            return file;
        }
        return null;
    }

    String getVector() {
        return mData;
    }

    @Override
    public void onChange(String value) {
        mData = value;
        mImageViewer.refreshImage(value);
        mMainFrame.updateSaveMenu(mImageViewer.isImageValid());
    }

    void dispose() {
    }

}
